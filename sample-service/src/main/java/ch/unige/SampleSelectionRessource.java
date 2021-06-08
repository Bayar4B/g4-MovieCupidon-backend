package ch.unige;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.unige.domain.LobbyConfig;
import info.movito.themoviedbapi.*;
import info.movito.themoviedbapi.model.*;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.TmdbGenre;

import org.eclipse.microprofile.config.ConfigProvider;

import java.util.*;


@Path("/sample-selection")
public class SampleSelectionRessource {

    private String apiKey = ConfigProvider.getConfig().getValue("tmdb.apiKey", String.class);
    private TmdbApi tmdbApi = new TmdbApi(apiKey);

    private TmdbGenre tmdbGenre = tmdbApi.getGenre();

    private Map<String, Integer> genreHashMap = generateGenreHashMap();

    private int sizeSample = 20;

    private Map<String, Integer> generateGenreHashMap(){
        Map<String, Integer> res = new HashMap<>();
        for (Genre g : tmdbGenre.getGenreList("en")){
            res.put(g.getName().toLowerCase(Locale.ROOT), g.getId());
        }
        return res;
    }


    @POST
    @Path("/get-sample")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getSample(LobbyConfig config){

        int sizeGenreList = config.getGenreList().length;
        if (sizeGenreList > 3 || sizeGenreList < 1){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        int[] genreIdList = new int[sizeGenreList];

        int i = 0;
        for (String genreParam : config.getGenreList()){
            if (genreHashMap.get(genreParam.toLowerCase(Locale.ROOT)) != null){
                genreIdList[i] = genreHashMap.get(genreParam.toLowerCase(Locale.ROOT));
                i++;
            }
            else{
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }

        int[] genreNumbersMovies = new int[sizeGenreList];
        int sum = 0;
        for (i = 0; i < sizeGenreList; i++) {
            if (i == sizeGenreList - 1){
                genreNumbersMovies[i] = sizeSample - sum;
            }
            else{
                genreNumbersMovies[i] = sizeSample / sizeGenreList;
                sum += sizeSample / sizeGenreList;
            }
        }

        ArrayList<MovieDb> sample = new ArrayList<>();
        TmdbDiscover disco = tmdbApi.getDiscover();
        i = 0;
        for (int genreID: genreIdList){
            String genreIDStr = Integer.toString(genreID);
            String yearDown = config.getRangeYear()[0] +"-01-01";
            String yearUp = config.getRangeYear()[1] +"-12-31";

            int pageCounter = 1;
            MovieResultsPage res = disco.getDiscover(pageCounter, "en-US", "vote_average.desc",
                    false, -1, -1, 35, 0, genreIDStr,
                    yearDown, yearUp,
                    "", "", "");

            int j = 0;
            for (Iterator<MovieDb> it = res.iterator(); it.hasNext(); ) {
                MovieDb movie = it.next();
                if (j == genreNumbersMovies[i]){
                    break;
                }

                // Si jamais le film est déjà présent dans la liste de sample
                while (sample.contains(movie)) {
                    movie = it.next();

                    // Si jamais il n'y a plus de films disponible dans les résultats, on prends ceux de la page suivante
                    if (!it.hasNext()){
                        pageCounter++;
                        res = disco.getDiscover(pageCounter, "en-US", "vote_average.desc",
                                false, -1, -1, 25, 0, genreIDStr,
                                yearDown, yearUp,
                                "", "", "");
                        it = res.iterator();
                    }
                }

                // Ajoute le film au sample
                sample.add(movie);
                j++;

                // Si jamais il faut encore selectionner des films pour le genre i
                if (!it.hasNext() && j != genreNumbersMovies[i]){
                    pageCounter++;
                    res = disco.getDiscover(pageCounter, "en-US", "vote_average.desc",
                            false, -1, -1, 25, 0, genreIDStr,
                            yearDown, yearUp,
                            "", "", "");
                    it = res.iterator();
                }
            }
            i++;
        }
        return Response.ok(sample).build();
    }

    @GET
    @Path("/helloworld")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {

        return "This is sample selection service";
    }



}

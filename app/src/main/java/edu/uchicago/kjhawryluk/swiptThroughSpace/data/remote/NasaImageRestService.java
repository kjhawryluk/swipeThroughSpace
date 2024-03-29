package edu.uchicago.kjhawryluk.swiptThroughSpace.data.remote;

import java.util.List;

import edu.uchicago.kjhawryluk.swiptThroughSpace.data.remote.model.NasaImageQueryResponse;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * https://android.jlelse.eu/rest-api-on-android-made-simple-or-how-i-learned-to-stop-worrying-and-love-the-rxjava-b3c2c949cad4
 */

public interface NasaImageRestService {
    @GET("search")
    Single<NasaImageQueryResponse> searchImages(@Query("q") String query,
                                                @Query("page") int page,
                                                @Query("media_type") String mediaType);

    @GET
    Single<List<String>> getLinks(@Url String url);

}

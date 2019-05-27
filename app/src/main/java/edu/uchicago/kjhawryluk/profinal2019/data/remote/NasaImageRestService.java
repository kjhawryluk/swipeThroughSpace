package edu.uchicago.kjhawryluk.profinal2019.data.remote;

import edu.uchicago.kjhawryluk.profinal2019.data.remote.model.NasaImageQueryResponse;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * https://android.jlelse.eu/rest-api-on-android-made-simple-or-how-i-learned-to-stop-worrying-and-love-the-rxjava-b3c2c949cad4
 */

public interface NasaImageRestService {
    @GET
    Single<NasaImageQueryResponse> searchImages(@Query("q") String query,
                                                @Query("page") int page,
                                                @Query("media_type") String mediaType);
}

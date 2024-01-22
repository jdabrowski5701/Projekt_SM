package sm.projekt;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class DictionaryApiService {

    private static final String BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";

    public static void getWordDefinition(String word, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL + word)
                .build();

        client.newCall(request).enqueue(callback);
    }
}

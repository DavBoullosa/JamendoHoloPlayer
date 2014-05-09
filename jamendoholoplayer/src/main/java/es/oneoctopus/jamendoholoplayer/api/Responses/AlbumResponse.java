
package es.oneoctopus.jamendoholoplayer.api.Responses;

import java.util.List;

public class AlbumResponse {
    private Headers headers;
    private List results;

    public Headers getHeaders() {
        return this.headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public List getResults() {
        return this.results;
    }

    public void setResults(List results) {
        this.results = results;
    }
}

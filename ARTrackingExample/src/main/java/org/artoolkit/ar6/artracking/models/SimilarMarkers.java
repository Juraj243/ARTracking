package org.artoolkit.ar6.artracking.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by krist on 04-Mar-18.
 */

public class SimilarMarkers {
    private List<String> images;
    private List<String> count;

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getCount() {
        return Integer.parseInt(count.get(0));
    }

    public void setCount(int count) {
        this.count = new ArrayList<>();
        this.count.add(Integer.toString(count));
    }
}

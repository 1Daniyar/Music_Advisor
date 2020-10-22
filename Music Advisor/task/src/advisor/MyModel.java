package advisor;

import java.util.Arrays;
import java.util.List;

public class MyModel {

    private List<String> names;
    private List<String> refs;
    private List<List<String>> artists;

    public MyModel(List<String> names) {
        this.names = names;
    }

    public MyModel(List<String> names, List<String> refs) {
        this.names = names;
        this.refs = refs;
    }

    public MyModel(List<String> names, List<String> refs, List<List<String>> artists) {
        this.names = names;
        this.refs = refs;
        this.artists = artists;
    }

    public void listOfElem1(int currentPage, int quantityOfElem) {
        for(int i = (currentPage - 1) * quantityOfElem; i < names.size() &&
                i < currentPage * quantityOfElem; i++) {
            System.out.println(names.get(i));
        }
    }

    public void listOfElem2(int currentPage, int quantityOfElem) {
        for(int i = (currentPage - 1) * quantityOfElem; i < names.size() &&
                i < currentPage * quantityOfElem; i++) {
            System.out.println(names.get(i));
            System.out.println(refs.get(i) + "\n");
        }
    }

    public void listOfElem3(int currentPage, int quantityOfElem) {
        for(int i = (currentPage - 1) * quantityOfElem; i < names.size() &&
                i < currentPage * quantityOfElem; i++) {
            System.out.println(names.get(i));
            System.out.println(Arrays.toString(artists.get(i).toArray()));
            System.out.println(refs.get(i) + "\n");
        }
    }

    public int getSize() {
        return names.size();
    }

    public void addName(String name) {
        names.add(name);
    }

    public String getName(int index) {
        return names.get(index);
    }

    public void addReference(String reference) {
        refs.add(reference);
    }

    public String getReference(int index) {
        return refs.get(index);
    }

    public void addArtists(List<String> list) {
        artists.add(list);
    }

    public List<String> getArtists(int index) {
        return artists.get(index);
    }
}

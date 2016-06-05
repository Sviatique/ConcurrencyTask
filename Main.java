import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Oleg on 21.05.2016.
 */
public class Main {
    final static int THREAD_COUNT = 3;
    public static void main(String[] args) {
        String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam vulputate mi mauris, vitae ultrices nunc vehicula eu. Ut laoreet luctus lorem, eget molestie lacus pretium eget. Nam ornare tortor vitae semper sodales. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Proin ut faucibus dui. Cras at tortor tempor, varius nisi non, euismod velit. Vivamus vulputate quam vitae nibh hendrerit, at ultrices dolor viverra. Proin imperdiet mollis urna. Aenean cursus tincidunt tempus. Aliquam erat volutpat. Curabitur congue, ante eget finibus venenatis, nibh sem tristique quam, sit amet imperdiet nisi diam a urna. Etiam luctus dolor non venenatis sollicitudin.\n" +
                "\n" +
                "Duis non vulputate lectus, vitae ullamcorper nisi. In non iaculis erat. Fusce et libero magna. Phasellus aliquet in justo non luctus. Nullam elementum dolor non facilisis vestibulum. Aenean in venenatis arcu, in porta lectus. Donec consectetur egestas tristique. Vivamus id lorem ac tortor blandit lacinia ac a quam. Curabitur aliquam sollicitudin massa sed congue. Curabitur nisi quam, efficitur eget sagittis quis, aliquet nec magna. Suspendisse potenti. Vivamus blandit porta lectus in sagittis. Morbi scelerisque dui leo, id faucibus lacus rutrum quis. Proin aliquet justo elementum, varius orci vel, aliquet magna.\n" +
                "\n" +
                "Aenean quis neque non ex malesuada dignissim in quis arcu. Cras ut erat nibh. Suspendisse at orci pharetra augue imperdiet congue nec vitae diam. Sed nec enim ut sem iaculis auctor. Maecenas risus turpis, pulvinar eget pharetra et, pretium sed tortor. Duis fermentum nunc vel nulla congue sollicitudin. Sed hendrerit et eros faucibus cursus. Donec ac arcu blandit ipsum malesuada aliquet. Quisque tincidunt non diam vitae eleifend. In ultricies mi in tortor maximus pretium. Donec dictum massa ut tortor condimentum, at lacinia libero sollicitudin. Vestibulum sed nunc et neque hendrerit elementum. Donec eleifend ex non magna pellentesque pulvinar. Duis ipsum nisi, convallis vel lobortis sed, mollis sed velit.\n" +
                "\n" +
                "Cras at odio risus. Donec massa nulla, cursus laoreet orci et, gravida maximus neque. Suspendisse pulvinar pharetra maximus. Nam dapibus eu est a varius. Praesent a ante interdum dui semper sodales consequat id tortor. Cras a felis odio. Nunc sit amet leo ac sem hendrerit dignissim pellentesque sit amet odio. Integer gravida sapien eu ipsum condimentum auctor. Suspendisse feugiat lorem rhoncus, tristique elit at, pulvinar sem. Mauris at erat placerat, ullamcorper lorem nec, elementum felis. Maecenas at vehicula eros, vitae pretium enim. Donec auctor augue erat, a hendrerit nibh semper in. Aliquam erat volutpat. Quisque condimentum mi eget tincidunt dapibus. Ut placerat hendrerit pulvinar.\n" +
                "\n" +
                "Morbi tincidunt quis urna eu consectetur. Fusce mattis magna tincidunt, consequat libero eu, malesuada nulla. In laoreet egestas neque sit amet eleifend. Aliquam nec elementum justo, sit amet suscipit felis. Sed feugiat rutrum tortor varius pharetra. Phasellus vel tristique est. Cras congue ultricies felis, vitae vehicula turpis molestie et. Sed pharetra magna eu blandit commodo. Integer consequat at felis ut hendrerit. Nunc vestibulum posuere cursus. Mauris sit amet hendrerit dui, nec convallis leo. Sed quis mauris ultrices, pellentesque eros sit amet, convallis augue.";
        String key = "Suspend";
        String[] splittedText = text.split("[ ]");
        //text = "1Suspend1 2Suspend2 3Suspend3 4Suspend4";
        List expectedIndexes = new ArrayList<>();
        List actualIndexes = Collections.synchronizedList(new ArrayList());



        int index = text.indexOf(key);
        while (index >= 0) {
            index = text.indexOf(key, index + 1);
        }
        int lastIndex = 0;
        int increment = text.length()/THREAD_COUNT;
        List<Runnable> threads = new ArrayList<>();
        for (int i = 0; i < THREAD_COUNT; i++) {

            int borderWordSize = 0;

            String partOfText = "";
            if(lastIndex + increment < text.length()) {
                partOfText = text.substring(lastIndex, lastIndex + increment);
                while(partOfText.charAt(partOfText.length()-1) != ' ' ){

                    borderWordSize++;
                    partOfText = partOfText.substring(0, partOfText.length()-1);
                }
            }
            else{
                partOfText = text.substring(lastIndex);
            }

            //System.out.println(partOfText.length());


            System.out.println("After: "+ partOfText);
            threads.add(new Thr(actualIndexes, lastIndex, partOfText, key));
            lastIndex += increment-borderWordSize;
            increment += borderWordSize;
            new Thread(threads.get(i)).start();
        }
        for (int i = 0; i < threads.size(); i++) {
            try {
                new Thread(threads.get(i)).join();
            } catch (InterruptedException e) {


            }
        }
        for (int i = 0; i < actualIndexes.size(); i++) {
            actualIndexes.get(i);
        }
    }

    public static void bubble_srt(List collection) {
        int n = collection.size();
        int k;
        for (int m = n; m >= 0; m--) {
            for (int i = 0; i < n - 1; i++) {
                k = i + 1;
                if (collection.get(i).equals(collection.get(k)) ) {
                    swapNumbers(i, k, collection);
                }
            }

        }
    }

    private static void swapNumbers(int i, int j, List collection) {

        Object temp;
        temp = collection.get(i);
        collection.set(i,collection.get(j));
        collection.set(j,temp);
    }

}

class Thr implements Runnable{
    String text;
    String key;
    int position;
    List actualIndexes;
    Thr(List actualIndexes, int position, String text, String key){
        this.text = text;
        this.key = key;
        this.position = position;
        this.actualIndexes = actualIndexes;
    }
    @Override
    public void run() {
        int index = text.indexOf(key);
        while (index >= 0) {
            System.out.println(index+position);
            actualIndexes.add(index+position);
            index = text.indexOf(key, index + 1);
        }
        try {
            new Thread(this).join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

class ECVA{
    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream file = new FileInputStream("ECVAdata - ECVA.csv");
        Scanner scan = new Scanner(file);
        ArrayList<String> teams = new ArrayList<String>();
        ArrayList<Double> lng = new ArrayList<Double>();
        ArrayList<Double> lat = new ArrayList<Double>();
        ArrayList<String> region = new ArrayList<String>();
        ArrayList<Integer> division = new ArrayList<Integer>();
        //Puts all the data in arrayLists.
        String s = scan.nextLine();
        while(scan.hasNext()){
            s = scan.nextLine();
            int split = s.indexOf(",");
            teams.add(s.substring(0,split));
            s = s.substring(split + 1);
            split = s.indexOf(",");
            lng.add(Double.parseDouble(s.substring(0,split)));
            s = s.substring(split + 1);
            split = s.indexOf(",");
            lat.add(Double.parseDouble(s.substring(0,split)));
            s = s.substring(split + 1);
            split = s.indexOf(",");
            region.add(s.substring(0,split));
            division.add(Integer.parseInt(s.substring(split+1)));
        }
        scan.close();
    }


    /**
     * 
     * @param teams Teams attending a tournament
     * @param lng Those teams longitude
     * @param lat Those team lattitude
     * @return The index of the best host, that is on average the closest to everyone
     */
    public static int bestHost(ArrayList<Double> lng, ArrayList<Double> lat){
        double best = -1;
        int host = -1;
        for(int i = 0; i < lng.size(); i++){
            double dist = 0;
            for(int j = 0; j < lng.size(); j++){
                dist += getDist(lng.get(i), lat.get(j), lng.get(i), lat.get(j));
            }
            if (dist < best || best < 0){
                best = dist;
                host = i;
            }
        }
        return host;
    }

    /**
     * Foundation from the METAL database.
     * Returns distance between two points in miles
     * @param lng1 Longitude of team 1
     * @param lat1 Latitude of team 1
     * @param lng2 Longitude of team 2
     * @param lat2 Latitude of team 2
     * @return distance between the two teams
     */
    public static double getDist(double lng1, double lat1, double lng2, double lat2){
       /** radius of the Earth in statute miles */
            final double EARTH_RADIUS = 3963.1;
        // coordinates in radians
            double rlat1 = Math.toRadians(lat1);
            double rlng1 = Math.toRadians(lng1);
            double rlat2 = Math.toRadians(lat2);
            double rlng2 = Math.toRadians(lng2);

        return Math.acos(Math.cos(rlat1) * Math.cos(rlng1) * Math.cos(rlat2) * Math.cos(rlng2) +
                Math.cos(rlat1) * Math.sin(rlng1) * Math.cos(rlat2) * Math.sin(rlng2) +
                Math.sin(rlat1) * Math.sin(rlat2)) * EARTH_RADIUS;
    }
}
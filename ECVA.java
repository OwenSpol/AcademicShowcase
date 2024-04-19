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
        ArrayList<Teams> teamList = new ArrayList<>();
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

        // Sorting data
        teamList = sortTeams(teams, lng, lat, region, division);

        
        //ArrayList of all region names
        ArrayList<String> regions = new ArrayList<String>();
        regions.add(teamList.get(0).getRegion() + " " + teamList.get(0).getDivision());
        for(int i = 1; i < teamList.size(); i ++){
            if(!(teamList.get(i - 1).getRegion() + teamList.get(i - 1).getDivision()).equals(teamList.get(i).getRegion() + teamList.get(i).getDivision())){
                regions.add(teamList.get(i).getRegion() + " " + teamList.get(i).getDivision());
            }
        }

        for(int i = 0; i < regions.size(); i++){
            ArrayList<Teams> inRegion = new ArrayList<Teams>();
            for(int j = 0; j < teamList.size(); j++){
                if(regions.get(i).equals(teamList.get(j).getRegion() + " " + teamList.get(j).getDivision())){
                    inRegion.add(teamList.get(j));
                }
            }
            int best = bestHost(inRegion);
            System.out.println(regions.get(i) + ": " + inRegion.get(best).getTeam());
        }
    }


    /**
     * 
     * @param teams Teams attending a tournament
     * @param lng Those teams longitude
     * @param lat Those team lattitude
     * @return The index of the best host, that is on average the closest to everyone
     */
    public static int bestHost(ArrayList<Teams> teamList){
        double best = -1;
        int host = -1;
        for(int i = 0; i < teamList.size(); i++){
            double dist = 0;
            for(int j = 0; j < teamList.size(); j++){
                dist += getDist(teamList.get(i).getLng(), teamList.get(i).getLat(), teamList.get(j).getLng(), teamList.get(j).getLat());
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
    
    /**
     * Method to sort the data by the region and division they are from
     * 
     * @param teams list of team names
     * @param lng list of latitudes corresponding with each team
     * @param lat list of longitudes corresponding with each team
     * @param region list of regions corresponding with each team
     * @param division list of divisions corresponding with each team
     */
    public static ArrayList<Teams> sortTeams (ArrayList<String> teams, ArrayList<Double> lng, ArrayList<Double> lat, ArrayList<String> region, ArrayList<Integer> division){
        // List of teams
        ArrayList<Teams> teamList = new ArrayList<>();

        // Adds all team info together into teamList
        for(int i = 0; i < teams.size(); i++){
            teamList.add(new Teams(teams.get(i), lng.get(i), lat.get(i), region.get(i), division.get(i)));
        }

        // Use a comparator to sort list
        Collections.sort(teamList, new Comparator<Teams>() {
            @Override
            public int compare(Teams team1, Teams team2){
                if(team1.getRegion().compareTo(team2.getRegion()) != 0){
                    return team1.getRegion().compareTo(team2.getRegion());
                }
                return Integer.compare(team1.getDivision(), team2.getDivision());
            }
        }); 
        return teamList;

        /*
        // Prints team region and divison to confirm correct sorting
        for(Teams teamData : teamList){
            System.out.println(teamData.getRegion() + " " + teamData.getDivision());
        }
        */
    }

    /**
     * Class to store the data about the teams
     */
    public static class Teams {
        private String team;
        private double lng;
        private double lat;
        private String region;
        private int division;

        public Teams (String team, double lng, double lat, String region, int division){
            this.team = team;
            this.lng = lng;
            this.lat = lat;
            this.region = region;
            this.division = division;
        }

        // Getter for team
        public String getTeam(){
            return team;
        }
        
        // Getter for longitude
        public double getLng(){
            return lng;
        }

        // Getter for latitude
        public double getLat(){
            return lat;
        }

        // Getter for region
        public String getRegion(){
            return region;
        }

        // Getter for division
        public int getDivision(){
            return division;
        }

    }
}
package workers;

import controllers.Controller;
import entities.Course;
import entities.Schedule;
import entities.Section;
import entities.Timeslot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageExporter extends Exporter {

    private final int height = 1195;
    private final int dayRowHeight = 70;
    private final int width = 1915;
    private final int timeColWidth = 100;
    private final int startHour = 7;
    private final int blockHeight;
    private final int blockWidth;
    private final Map<LocalTime, Integer> timeToRow;
    private final File outputDirectory;

    public ImageExporter(){
        blockHeight  = (height - dayRowHeight) / 15;
        blockWidth= (width - timeColWidth) / 5;
        timeToRow = new HashMap<>();

        for (int i = 0; i < 15; i++){
            timeToRow.put(LocalTime.of(i +startHour, 0), i);
        }
        String baseDir = new File("").getAbsolutePath();
        outputDirectory = new File(baseDir.concat("/output"));
        if (!outputDirectory.exists()) {
            outputDirectory.mkdir();
        }
    }


    @Override
    public void outputSchedule(Schedule schedule) {
        outputSchedule(schedule, "schedule_image" + num);
        num+=1;
    }

    @Override
    public void outputSchedule(Schedule schedule, String name) {
        List<Section> winterSections = new ArrayList<>();
        List<Section> fallSections = new ArrayList<>();

        for(Section lec: schedule.getLectures()){
            if(lec.getSession().equals("F")){
                fallSections.add(lec);
            }
            else if(lec.getSession().equals("S")){
                winterSections.add(lec);
            }
            else{
                fallSections.add(lec);
                winterSections.add(lec);
            }
        }
        for(Section tut: schedule.getTutorials()){
            if(tut.getSession().equals("F")){
                fallSections.add(tut);
            }
            else if(tut.getSession().equals("S")){
                winterSections.add(tut);
            }
            else{
                fallSections.add(tut);
                winterSections.add(tut);
            }
        }
        if(winterSections.size() > 0){
            BufferedImage image = drawSchedule(winterSections);
            try {
                ImageIO.write(image, "jpg", new File( outputDirectory.getAbsolutePath().concat("/" +name +"_winter.jpg")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(fallSections.size()>0){
            BufferedImage image = drawSchedule(fallSections);
            try {
                ImageIO.write(image, "jpg", new File( outputDirectory.getAbsolutePath().concat("/" +name +"_fall.jpg")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private BufferedImage drawSchedule(List<Section> sections){

        BufferedImage bufferedImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fill(new Rectangle(0,0, width, height));
        drawGrid(graphics);

        int sessionsSeen = 0;
        for (Section lec: sections){
            Color color = getColor(sessionsSeen);
            sessionsSeen++;
            for (Timeslot timeslot: lec.getTimes()){
                drawTimeslot(graphics, lec.getName(), timeslot, color);
            }
        }
        return bufferedImage;
    }

    private void drawTimeslot(Graphics2D graphics, String name, Timeslot t, Color color){

        String timeslotLabel = name + "\n" + t.getStart().toString() + "-" + t.getEnd().toString() + " at " + t.getRoom();

        int hoursSpanned = t.getEnd().getHour() - t.getStart().getHour();



        int x = (t.getDay().getValue()-1) * blockWidth + timeColWidth;
        int y = timeToRow.get(t.getStart()) * blockHeight + dayRowHeight;
        graphics.setColor(color);
        Rectangle timeslotBlock = new Rectangle(x,y, blockWidth, blockHeight * hoursSpanned);
        graphics.fill(timeslotBlock);
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Arial Black", Font.BOLD, 20));
        drawMultilineString(graphics, timeslotLabel, x+10, y+10);
    }

    private void drawMultilineString(Graphics2D graphics, String text, int x, int y){
        // from https://stackoverflow.com/questions/4413132/problems-with-newline-in-graphics2d-drawstring
        for (String line: text.split("\n")){
            graphics.drawString(line, x, y+= graphics.getFontMetrics().getHeight());
        }
    }

    private void drawGrid(Graphics2D graphics){
        graphics.setColor(Color.BLACK);
        for (int col = 0; col < 6; col++){
            for (int row = 0; row < 16; row++){

                // Adjusted to account for having drawn the time column and day row
                int adjustedX = (col-1) * blockWidth + timeColWidth;
                int adjustedY = (row-1) * blockHeight + dayRowHeight;
                if (col == 0){
                    int timeBlockY = row * blockHeight + dayRowHeight;
                    String time = LocalTime.of(row + startHour,0).toString();
                    Rectangle timeBlock = new Rectangle(0, timeBlockY, timeColWidth, blockHeight);
                    graphics.setFont(new Font("Arial Black", Font.BOLD, 20));
                    graphics.drawString(time, 10,  timeBlockY + 25);
                    graphics.draw(timeBlock);
                }
                else if(row == 0){
                    DayOfWeek day = DayOfWeek.of(col);
                    Rectangle dayBlock = new Rectangle(adjustedX, 0, blockWidth, dayRowHeight);
                    graphics.setFont(new Font("Arial Black", Font.BOLD, 20));
                    graphics.drawString(day.toString(), adjustedX + 10, 25);
                    graphics.draw(dayBlock);
                }
                else {
                    Rectangle block = new Rectangle(adjustedX, adjustedY, blockWidth, blockHeight);
                    graphics.draw(block);
                }
            }

        }
    }

    /**
     *
     * @param sessionNum number of sessions seen before current session
     * @return a unique color to draw this session with
     */
    private Color getColor(int sessionNum){
        switch(sessionNum){
            case 0:
                return Color.decode("#ffe119");
            case 1: return
            Color.decode("#bfef45");
            case 2: return Color.decode("#42d4f4");
            case 3: return
                    Color.decode("#fabed4");
            case 4: return Color.decode("#ffd8b1");
            case 5: return
                    Color.decode("#fffac8");
            case 6: return
                    Color.decode("#aaffc3");
            case 7: return
                    Color.decode("#dcbeff");
            case 8: return
                    Color.decode("#f58231");
            case 9: return
                    Color.decode("#a9a9a9");
            default:
                return Color.pink;
        }
    }



}

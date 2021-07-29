package Algorithms.utils;

import javafx.scene.canvas.GraphicsContext;

public class Grid {
    private int size;
    private double width;
    private double height;
    private Point[][] grid;

    public Grid(int size, int width, int height) {
        this.size = size;
        this.height = height;
        this.width = width;
        this.grid = new Point[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = new Point(i + size,j + size);
            }
        }
    }

    public void drawGrid(GraphicsContext gc) {
//        int h = (int)height/size;
//        int w = (int)width/size;
//        for (int i = 0; i < height; i+=h) {
//            for (int j = 0; j < width; j+=w) {
//                gc.strokeRect(grid[i][j].x(),grid[i][j].y(),grid[i][j].x()+w,grid[i][j].y()+h);
//            }
//        }

//        // vertical lines
//        for (float i = 0f; i < width; i += size) {
//            gc.strokeLine(i, 0, i, height - (height % 30));
//        }
//
//        // horizontal lines
//        for (float y = 0.2f; y < height; y += size) {
//            gc.strokeLine(0, y, width, y);
//        }

    }

}

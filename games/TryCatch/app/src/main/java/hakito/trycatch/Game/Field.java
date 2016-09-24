package hakito.trycatch.Game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hakito.trycatch.Data.Models.Good;
import hakito.trycatch.Data.Models.Level;
import hakito.trycatch.Debug.Debug;
import hakito.trycatch.Game.Logic.Helper;
import hakito.trycatch.Game.Logic.PathFinder;
import hakito.trycatch.Game.Objects.BasicCell;
import hakito.trycatch.Game.Objects.Portal;
import hakito.trycatch.Game.Objects.RectangleAnimator;
import hakito.trycatch.Game.Objects.Toy;
import hakito.trycatch.Game.Objects.Cell;
import hakito.trycatch.Game.Objects.Cells;
import hakito.trycatch.Game.Objects.DrawableObject;
import hakito.trycatch.Game.Objects.UpdatableObject;
import hakito.trycatch.R;

/**
 * Created by Oleg on 12-Dec-15.
 */
public class Field {


    private BasicCell[][] cells;
    private Toy toy;
    List<UpdatableObject> updatableObjects;
    List<DrawableObject> drawableObjects;
    private Paint back, extrasense_paint, freeze_paint, teleport_paint;
    private Good.GoodType currentBonus;

    private Point bonus_extrasense;

    public Field() {
        extrasense_paint = new Paint();
        extrasense_paint.setColor(Game.get().getContext().getResources().getColor(R.color.game_toy));
        extrasense_paint.setStyle(Paint.Style.STROKE);

        teleport_paint = extrasense_paint;

        freeze_paint = new Paint();
        freeze_paint.setColor(Game.get().getContext().getResources().getColor(R.color.game_froze));
        freeze_paint.setStyle(Paint.Style.STROKE);
        freeze_paint.setStrokeWidth(10);


        currentBonus = null;
        back = new Paint();
        back.setColor(Game.get().getContext().getResources().getColor(R.color.game_field));

        Level level = Game.get().getLevel();
        cells = new BasicCell[level.getSize()][level.getSize()];

        updatableObjects = new ArrayList<>(level.getSize() * level.getSize());
        drawableObjects = new ArrayList<>(level.getSize() * level.getSize());

        for (int x = 0; x < level.getSize(); x++) {
            for (int y = 0; y < level.getSize(); y++) {
                cells[x][y] = new Cell(x, y, Cells.EMPTY);
                updatableObjects.add(cells[x][y]);
                drawableObjects.add(cells[x][y]);
            }
        }

        toy = new Toy(level.getSize() / 2, level.getSize() / 2);



        init(level);

        drawableObjects.add(toy);
        updatableObjects.add(toy);
    }

    void init(Level level) {
        if(level.isWithTeleport())
        {
            createPortal();
        }
        addCells(level.getBlocksCount(), Cells.BLOCK, level.getSize());
        addCells(level.getCoinsCount(), Cells.COIN, level.getSize());

    }

    private void removeCell(Point point)
    {
        drawableObjects.remove(cells[point.x][point.y]);
        updatableObjects.remove(cells[point.x][point.y]);

    }

    private void createPortal()
    {
        Point point = getFreeCellForPortal(1);
        Portal out = new Portal(point.x, point.y, null);
        drawableObjects.add(out);
        updatableObjects.add(out);
        removeCell(point);
        cells[point.x][point.y] = out;

        point = getFreeCellForPortal(Game.get().getLevel().getSize() / 2 - 2);
        Portal in = new Portal(point.x, point.y, out);
        removeCell(point);
        drawableObjects.add(in);
        updatableObjects.add(in);
        cells[point.x][point.y] = in;

    }

    private Point getFreeCellForPortal(int distFromAside) {
        Point res = Helper.getRandomPointByDistFromAside(Game.get().getLevel().getSize(), distFromAside);
        while (!cells[res.x][res.y].isFree() || res.equals(toy.position()))
            res = Helper.getRandomPoint(Game.get().getLevel().getSize());
        return res;
    }

    void deactivateBonus()
    {
        currentBonus=null;
        Game.get().setHint(R.string.certain_hint);
    }

    public void activateBonus(Good good) {
        currentBonus = good.getType();
        switch (good.getType()) {
            case FROZE:
                Game.get().setHint(R.string.froze_hint);
                break;
            case EXTRASENSE:
                Game.get().setHint(R.string.extrasense_hint);
                bonus_extrasense = Toy.getNewPoint(toy.position(), PathFinder.getPath());
                break;
            case TELEPORT:
                Game.get().setHint(R.string.teleport_hint);
                break;
        }

    }

    void addCells(int count, Cells type, int size) {
        Random random = new Random();
        for (int x = 0; x < count; x++) {
            int ix = random.nextInt(size), iy = random.nextInt(size);
            BasicCell bc = cells[ix][iy];
            boolean added=false;
            if(bc instanceof Cell) {
                Cell cell = (Cell)bc;
                if (cell.isFree() && ix != toy.position().x && iy != toy.position().y) {
                    cell.setType(type);
                    added = true;
                }
            }
            if(!added)
                x--;

        }
    }

    public BasicCell[][] getCells() {
        return cells;
    }


    public Toy getToy() {
        return toy;
    }

    public void touch(int x, int y) {
        int cx = x / Game.L, cy = y / Game.L;
        Debug.x = cx;
        Debug.y = cy;

        //skip current toy`s step
        boolean skip = false;
        if (currentBonus != null) {
            switch (currentBonus) {
                case FROZE:
                    skip = true;

                    break;
                case EXTRASENSE:
                    deactivateBonus();
                    break;
                case TELEPORT:
                    if (Helper.isCorrectIndices(cx, cy, Game.get().getLevel().getSize()) && (toy.position().x != cx || toy.position().y != cy)) {
                        if (cells[cx][cy].isFree()) {
                            toy.teleport(cx, cy);
                            deactivateBonus();
                            toy.step(true, null);
                            return;
                        }
                    }

                    break;
            }
        }
        if (Helper.isCorrectIndices(cx, cy, Game.get().getLevel().getSize()) && (toy.position().x != cx || toy.position().y != cy)) {
            if(cells[cx][cy] instanceof Cell) {
                if (((Cell)cells[cx][cy]).pick()) {
                    if (currentBonus == Good.GoodType.FROZE) deactivateBonus();
                    toy.step(skip, new RectangleAnimator.OnAnimatorfinishListener() {
                        @Override
                        public void finished() {
                            if(cells[toy.getX()][toy.getY()] instanceof Portal)
                            {
                                Portal potal = (Portal)cells[toy.getX()][toy.getY()];
                                if(potal.getPortalType() == Portal.PortalType.IN)
                                {
                                    toy.teleport(potal.getNeighbour().getX(), potal.getNeighbour().getY());
                                    toy.step(true, null);
                                }
                            }
                        }
                    });

                    Game.get().getGameResult().steps++;
                }
            }
        }
    }


    public void update(float d) {
        for (UpdatableObject o : updatableObjects) {
            o.update(d);
        }
    }

    public void draw(Canvas c) {

        c.drawRect(new RectF(0, 0, Game.get().getSizePX().x, Game.get().getSizePX().y), back);

        for (DrawableObject o : drawableObjects) {
            o.draw(c);
        }
        if (currentBonus != null) {
            switch (currentBonus) {
                case FROZE:
                    c.drawOval(Helper.getRectF(Game.getCellRect(toy.position().x, toy.position().y)), freeze_paint);
                    break;
                case EXTRASENSE:
                    RectF rect = Helper.getRectF(Game.getCellRect(bonus_extrasense.x, bonus_extrasense.y));
                    rect.inset(20, 20);
                    c.drawOval(rect, extrasense_paint);
                    break;
                case TELEPORT:
                    for (DrawableObject o : drawableObjects) {
                        if (o instanceof Cell) {
                            Cell cell = (Cell) o;
                            if (cell.getX() != toy.position().x || cell.getY() != toy.position().y) {
                                if (((Cell) o).isFree()) {
                                    c.drawOval(Helper.getRectF(Game.getCellRect(((Cell) o).getX(), ((Cell) o).getY())), teleport_paint);
                                }
                            }
                        }
                    }
                    break;
            }
        }
        final Paint p = new Paint();
        p.setColor(Color.BLUE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);

        //   c.drawRect(Game.getCellRect(Debug.x, Debug.y), p);
    }
}

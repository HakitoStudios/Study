package hakito.trycatch.Data.Models;

/**
 * Created by Oleg on 27-Dec-15.
 */
public class Level {
    private int index, size, coinsCount, blocksCount, starsToOpen;
    private boolean withTeleport;

    public Level(int index, int size, int coinsCount, int blocksCount, int starsToOpen, boolean withTeleport) {
    this(index, size, coinsCount, blocksCount, starsToOpen);

        this.withTeleport = withTeleport;
    }

    public Level(int index, int size, int coinsCount, int blocksCount, int starsToOpen) {
        this.index = index;
        this.size = size;
        this.coinsCount = coinsCount;
        this.blocksCount = blocksCount;
        this.starsToOpen = starsToOpen;
        withTeleport = size  > 7;
    }

    public int getSize() {
        return size;
    }

    public int getIndex() {
        return index;
    }

    public int getCoinsCount() {
        return coinsCount;
    }

    public int getBlocksCount() {
        return blocksCount;
    }

    public int getStarsToOpen() {
        return starsToOpen;
    }

    public boolean isWithTeleport() {
        return withTeleport;
    }
}

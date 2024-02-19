@file:Suppress("SpellCheckingInspection", "FunctionName", "RedundantSemicolon", "MemberVisibilityCanBePrivate")

package game

import com.badlogic.gdx.Input
import intensigame.IntensiScreen
import intensigame.graphics.*
import intensigame.log.Log
import kotlin.math.floor
import kotlin.math.roundToInt

//enum class KEY(val code: Int) {
//    ENTER(13),
//    ESC(27),
//    SPACE(32),
//    PAGEUP(33),
//    PAGEDOWN(34),
//    LEFT(37),
//    UP(38),
//    RIGHT(39),
//    DOWN(40);
//}

fun timestamp() = System.currentTimeMillis()
fun random(min: Double, max: Double) = (min + (Math.random() * (max - min)));
fun randomInt(min: Int, max: Int) = floor(random(min.toDouble(), max.toDouble())).toInt()
fun <T> randomChoice(choices: Array<T>): T = choices[Math.round(random(0.0, choices.size - 1.0)).toInt()];

var DIRX = intArrayOf(0, 1, 1, 1, 0, -1, -1, -1, 0)
var DIRY = intArrayOf(-1, -1, 0, 1, 1, 1, 0, -1, 0)

fun rotateLeft(dir: Int) = (dir - 2) + if (dir < 2) 8 else 0
fun rotateRight(dir: Int) = (dir + 2) - if (dir > 5) 8 else 0
fun horizontal(dir: DIR) = horizontal(dir.ordinal)
fun horizontal(dir: Int) = (dir == DIR.LEFT.ordinal) || (dir == DIR.RIGHT.ordinal)
fun vertical(dir: DIR) = (dir == DIR.UP) || (dir == DIR.DOWN)

data class BdSprite(
    val x: Int,
    val y: Int,
    val f: Int = 0,
    val fps: Int = 0,
)

enum class OBJECT(
    val code: Int,
    val rounded: Boolean,
    val explodable: Boolean,
    val consumable: Boolean,
    val sprite: BdSprite,
    val flash: BdSprite? = null,
    val left: BdSprite? = null,
    val right: BdSprite? = null,
    val blink: BdSprite? = null,
    val tap: BdSprite? = null,
    val blinktap: BdSprite? = null,
) {
    SPACE(code = 0x00, rounded = false, explodable = false, consumable = true, sprite = BdSprite(x = 0, y = 6), flash = BdSprite(x = 4, y = 0)),
    DIRT(code = 0x01, rounded = false, explodable = false, consumable = true, sprite = BdSprite(x = 1, y = 7)),
    BRICKWALL(code = 0x02, rounded = true, explodable = false, consumable = true, sprite = BdSprite(x = 3, y = 6)),
    MAGICWALL(code = 0x03, rounded = false, explodable = false, consumable = true, sprite = BdSprite(x = 4, y = 6, f = 4, fps = 20)),
    PREOUTBOX(code = 0x04, rounded = false, explodable = false, consumable = false, sprite = BdSprite(x = 1, y = 6)),
    OUTBOX(code = 0x05, rounded = false, explodable = false, consumable = false, sprite = BdSprite(x = 1, y = 6, f = 2, fps = 4)),
    STEELWALL(code = 0x07, rounded = false, explodable = false, consumable = false, sprite = BdSprite(x = 1, y = 6)),
    FIREFLY1(code = 0x08, rounded = false, explodable = true, consumable = true, sprite = BdSprite(x = 0, y = 9, f = 8, fps = 20)),
    FIREFLY2(code = 0x09, rounded = false, explodable = true, consumable = true, sprite = BdSprite(x = 0, y = 9, f = 8, fps = 20)),
    FIREFLY3(code = 0x0A, rounded = false, explodable = true, consumable = true, sprite = BdSprite(x = 0, y = 9, f = 8, fps = 20)),
    FIREFLY4(code = 0x0B, rounded = false, explodable = true, consumable = true, sprite = BdSprite(x = 0, y = 9, f = 8, fps = 20)),
    BOULDER(code = 0x10, rounded = true, explodable = false, consumable = true, sprite = BdSprite(x = 0, y = 7)),
    BOULDERFALLING(code = 0x12, rounded = false, explodable = false, consumable = true, sprite = BdSprite(x = 0, y = 7)),
    DIAMOND(code = 0x14, rounded = true, explodable = false, consumable = true, sprite = BdSprite(x = 0, y = 10, f = 8, fps = 20)),
    DIAMONDFALLING(code = 0x16, rounded = false, explodable = false, consumable = true, sprite = BdSprite(x = 0, y = 10, f = 8, fps = 20)),
    EXPLODETOSPACE0(code = 0x1B, rounded = false, explodable = false, consumable = false, sprite = BdSprite(x = 3, y = 7)),
    EXPLODETOSPACE1(code = 0x1C, rounded = false, explodable = false, consumable = false, sprite = BdSprite(x = 4, y = 7)),
    EXPLODETOSPACE2(code = 0x1D, rounded = false, explodable = false, consumable = false, sprite = BdSprite(x = 5, y = 7)),
    EXPLODETOSPACE3(code = 0x1E, rounded = false, explodable = false, consumable = false, sprite = BdSprite(x = 4, y = 7)),
    EXPLODETOSPACE4(code = 0x1F, rounded = false, explodable = false, consumable = false, sprite = BdSprite(x = 3, y = 7)),
    EXPLODETODIAMOND0(code = 0x20, rounded = false, explodable = false, consumable = false, sprite = BdSprite(x = 3, y = 7)),
    EXPLODETODIAMOND1(code = 0x21, rounded = false, explodable = false, consumable = false, sprite = BdSprite(x = 4, y = 7)),
    EXPLODETODIAMOND2(code = 0x22, rounded = false, explodable = false, consumable = false, sprite = BdSprite(x = 5, y = 7)),
    EXPLODETODIAMOND3(code = 0x23, rounded = false, explodable = false, consumable = false, sprite = BdSprite(x = 4, y = 7)),
    EXPLODETODIAMOND4(code = 0x24, rounded = false, explodable = false, consumable = false, sprite = BdSprite(x = 3, y = 7)),
    PREROCKFORD1(code = 0x25, rounded = false, explodable = false, consumable = false, sprite = BdSprite(x = 1, y = 6, f = 2, fps = 4)),
    PREROCKFORD2(code = 0x26, rounded = false, explodable = false, consumable = false, sprite = BdSprite(x = 1, y = 0)),
    PREROCKFORD3(code = 0x27, rounded = false, explodable = false, consumable = false, sprite = BdSprite(x = 2, y = 0)),
    PREROCKFORD4(code = 0x28, rounded = false, explodable = false, consumable = false, sprite = BdSprite(x = 3, y = 0)),
    BUTTERFLY1(code = 0x30, rounded = false, explodable = true, consumable = true, sprite = BdSprite(x = 0, y = 11, f = 8, fps = 20)),
    BUTTERFLY2(code = 0x31, rounded = false, explodable = true, consumable = true, sprite = BdSprite(x = 0, y = 11, f = 8, fps = 20)),
    BUTTERFLY3(code = 0x32, rounded = false, explodable = true, consumable = true, sprite = BdSprite(x = 0, y = 11, f = 8, fps = 20)),
    BUTTERFLY4(code = 0x33, rounded = false, explodable = true, consumable = true, sprite = BdSprite(x = 0, y = 11, f = 8, fps = 20)),
    ROCKFORD(code = 0x38, rounded = false, explodable = true, consumable = true, sprite = BdSprite(x = 0, y = 0),   // standing still
        left = BdSprite(x = 0, y = 4, f = 8, fps = 20),   // running left
        right = BdSprite(x = 0, y = 5, f = 8, fps = 20),   // running right
        blink = BdSprite(x = 0, y = 1, f = 8, fps = 20),   // blinking
        tap = BdSprite(x = 0, y = 2, f = 8, fps = 20),   // foot tapping
        blinktap = BdSprite(x = 0, y = 3, f = 8, fps = 20)), // foot tapping and blinking
    AMOEBA(code = 0x3A, rounded = false, explodable = false, consumable = true, sprite = BdSprite(x = 0, y = 8, f = 8, fps = 20)),
}

fun Int.obj() = OBJECT.entries.first { it.code == this }

val FIREFLIES = mapOf(
    DIR.LEFT to OBJECT.FIREFLY1,
    DIR.UP to OBJECT.FIREFLY2,
    DIR.RIGHT to OBJECT.FIREFLY3,
    DIR.DOWN to OBJECT.FIREFLY4,
).mapKeys { it.key.ordinal }
val BUTTERFLIES = mapOf(
    DIR.LEFT to OBJECT.BUTTERFLY1,
    DIR.UP to OBJECT.BUTTERFLY2,
    DIR.RIGHT to OBJECT.BUTTERFLY3,
    DIR.DOWN to OBJECT.BUTTERFLY4,
).mapKeys { it.key.ordinal }
val PREROCKFORDS = listOf(
    OBJECT.PREROCKFORD1,
    OBJECT.PREROCKFORD2,
    OBJECT.PREROCKFORD3,
    OBJECT.PREROCKFORD4,
    OBJECT.ROCKFORD,
)

val EXPLODETOSPACE = listOf(
    OBJECT.EXPLODETOSPACE0,
    OBJECT.EXPLODETOSPACE1,
    OBJECT.EXPLODETOSPACE2,
    OBJECT.EXPLODETOSPACE3,
    OBJECT.EXPLODETOSPACE4,
    OBJECT.SPACE,
)

val EXPLODETODIAMOND = listOf(
    OBJECT.EXPLODETODIAMOND0,
    OBJECT.EXPLODETODIAMOND1,
    OBJECT.EXPLODETODIAMOND2,
    OBJECT.EXPLODETODIAMOND3,
    OBJECT.EXPLODETODIAMOND4,
    OBJECT.DIAMOND,
)

fun isButterfly(o: OBJECT) = (OBJECT.BUTTERFLY1.code <= o.code) && (o.code <= OBJECT.BUTTERFLY4.code)

data class BdPoint(var x: Int, var y: Int)

fun Point(x: Int, y: Int, dir: Int) = BdPoint(x + DIRX[dir], y + DIRY[dir])

data class BdCell(
    val p: BdPoint = BdPoint(0, 0),
    var frame: Int = 0,
    var obj: OBJECT = OBJECT.SPACE,
    var invalid: Boolean = false,
)

data class Idle(var blink: Boolean = false, var tap: Boolean = false)
data class Diamonds(var collected: Int, var needed: Int, var value: Int, var extra: Int)
data class Magic(var active: Boolean, var time: Double)
data class Amoeba(
    val max: Int,
    var slow: Double,
    var size: Int = 0,
    var enclosed: Boolean = false,
    var dead: OBJECT? = null,
)

//=========================================================================
// GAME LOGIC
//=========================================================================

object Game {
    //    val n = level.coerceIn(CAVES.indices)
    var index = 0;        // cave index
    lateinit var cave: Cave;             // cave definition
    var width = 0;               // cave cell width
    var height = 0;              // cave cell height
    var cells = Array(40) { Array(22) { BdCell() } };    // will be built up into 2-dimensional array below
    var frame = 0;                             // game frame counter starts at zero
    var fps = 0;                            // how many game frames per second
    var step = 0.0;                    // how long is each game frame (in seconds)
    var birth = 0;                    // in which frame is rockford born ?
    var timer = 0;            // seconds allowed to complete this cave
    var score = 0;

    var flash = 0;                         // trigger white flash when rockford has collected enought diamonds
    var won = false;                         // set to true when rockford enters the outbox
    var foundRockford = 0

    lateinit var idle: Idle // is rockford showing any idle animation ?
    lateinit var diamonds: Diamonds
    lateinit var amoeba: Amoeba
    lateinit var magic: Magic

    fun reset(level: Int = index) {
        val n = level.coerceIn(CAVES.indices)
        //n = Math.min(CAVES.length-1, Math.max(0, (typeof n === 'number' ? n : this.storage.level || 0)));
        this.index = n;        // cave index
        this.cave = CAVES[this.index];             // cave definition
        this.width = this.cave.width;               // cave cell width
        this.height = this.cave.height;              // cave cell height
        this.cells = Array(width) { Array(height) { BdCell() } }; // will be built up into 2-dimensional array below
        this.frame = 0;                             // game frame counter starts at zero
        this.fps = 10;                            // how many game frames per second
        this.step = 1.0 / this.fps;                    // how long is each game frame (in seconds)
        this.birth = 2 * this.fps;                    // in which frame is rockford born ?
        this.timer = this.cave.caveTime;            // seconds allowed to complete this cave
        this.idle = Idle(blink = false, tap = false);  // is rockford showing any idle animation ?
        this.flash = 0;                         // trigger white flash when rockford has collected enought diamonds
        this.won = false;                         // set to true when rockford enters the outbox
        this.diamonds = Diamonds(
            collected = 0,
            needed = this.cave.diamondsNeeded,
            value = this.cave.initialDiamondValue,
            extra = this.cave.extraDiamondValue
        );
        this.amoeba = Amoeba(
            max = this.cave.amoebaMaxSize,
            slow = this.cave.amoebaSlowGrowthTime / this.step
        );
        this.magic = Magic(
            active = false,
            time = this.cave.magicWallMillingTime / this.step,
        )

        for (y in 0 until height) {
            for (x in 0 until width) {
                this.cells[x][y] = BdCell(p = BdPoint(x, y), frame = 0, obj = this.cave.map[x][y].obj())
            }
        }
        this.publish("level", this.cave);
    }

    fun prev() = if (this.index > 0) this.reset(this.index - 1); else Unit
    fun next() = if (this.index < CAVES.size - 1) this.reset(this.index + 1); else Unit

    fun get(p: BdPoint, dir: DIR = DIR.NONE) = cells[p.x + (DIRX[dir.ordinal])][p.y + (DIRY[dir.ordinal])].obj
    fun get(p: BdPoint, dir: Int = DIR.NONE.ordinal) = cells[p.x + (DIRX[dir])][p.y + (DIRY[dir])].obj

    fun set(p: BdPoint, o: OBJECT) = set(p, o, DIR.NONE.ordinal)
    fun set(p: BdPoint, o: OBJECT, dir: Int = DIR.NONE.ordinal) {
        val cell = cells[p.x + (DIRX[dir])][p.y + (DIRY[dir])];
        cell.obj = o;
        cell.frame = frame;
        publish("cell", cell)
    }

    fun move(p: BdPoint, dir: DIR, o: OBJECT) = also { clear(p); set(p, o, dir.ordinal); }
    fun move(p: BdPoint, dir: Int, o: OBJECT) = also { clear(p); set(p, o, dir); }
    fun clear(p: BdPoint, dir: DIR) = set(p, OBJECT.SPACE, dir.ordinal)
    fun clear(p: BdPoint, dir: Int = DIR.NONE.ordinal) = set(p, OBJECT.SPACE, dir)
    fun isempty(p: BdPoint, dir: DIR) = get(p, dir) == OBJECT.SPACE
    fun isempty(p: BdPoint, dir: Int = DIR.NONE.ordinal) = get(p, dir) == OBJECT.SPACE
    fun isdirt(p: BdPoint, dir: DIR) = get(p, dir) == OBJECT.DIRT
    fun isboulder(p: BdPoint, dir: DIR) = get(p, dir) == OBJECT.BOULDER
    fun isrockford(p: BdPoint, dir: DIR) = get(p, dir) == OBJECT.ROCKFORD
    fun isdiamond(p: BdPoint, dir: DIR) = get(p, dir) == OBJECT.DIAMOND
    fun isamoeba(p: BdPoint, dir: DIR) = get(p, dir) == OBJECT.AMOEBA
    fun ismagic(p: BdPoint, dir: DIR) = get(p, dir) == OBJECT.MAGICWALL
    fun isoutbox(p: BdPoint, dir: DIR) = get(p, dir) == OBJECT.OUTBOX
    fun isbutterfly(p: BdPoint, dir: Int = DIR.NONE.ordinal) = isButterfly(get(p, dir))
    fun isexplodable(p: BdPoint, dir: DIR) = get(p, dir).explodable
    fun isexplodable(p: BdPoint, dir: Int = DIR.NONE.ordinal) = get(p, dir).explodable
    fun isconsumable(p: BdPoint, dir: Int = DIR.NONE.ordinal) = get(p, dir).consumable
    fun isrounded(p: BdPoint, dir: DIR) = get(p, dir).rounded
    fun isfallingdiamond(p: BdPoint, dir: DIR) = get(p, dir) == OBJECT.DIAMONDFALLING

    fun eachCell(fn: Game.(BdCell) -> Unit) = eachCell(this, fn)
    fun <T : Any> eachCell(thisArg: T, fn: T.(BdCell) -> Unit) {
        for (y in 0 until height) {
            for (x in 0 until width) {
                fn(thisArg, cells[x][y])
            }
        }
    }

    fun update() {
        this.beginFrame()
        this.eachCell { cell ->
            if (cell.frame < this.frame) {
                when (cell.obj) {
                    OBJECT.PREROCKFORD1      -> this.updatePreRockford(cell.p, 1);
                    OBJECT.PREROCKFORD2      -> this.updatePreRockford(cell.p, 2);
                    OBJECT.PREROCKFORD3      -> this.updatePreRockford(cell.p, 3);
                    OBJECT.PREROCKFORD4      -> this.updatePreRockford(cell.p, 4);
                    OBJECT.ROCKFORD          -> this.updateRockford(cell.p, moving.dir);
                    OBJECT.BOULDER           -> this.updateBoulder(cell.p);
                    OBJECT.BOULDERFALLING    -> this.updateBoulderFalling(cell.p);
                    OBJECT.DIAMOND           -> this.updateDiamond(cell.p);
                    OBJECT.DIAMONDFALLING    -> this.updateDiamondFalling(cell.p);
                    OBJECT.FIREFLY1          -> this.updateFirefly(cell.p, DIR.LEFT);
                    OBJECT.FIREFLY2          -> this.updateFirefly(cell.p, DIR.UP);
                    OBJECT.FIREFLY3          -> this.updateFirefly(cell.p, DIR.RIGHT);
                    OBJECT.FIREFLY4          -> this.updateFirefly(cell.p, DIR.DOWN);
                    OBJECT.BUTTERFLY1        -> this.updateButterfly(cell.p, DIR.LEFT);
                    OBJECT.BUTTERFLY2        -> this.updateButterfly(cell.p, DIR.UP);
                    OBJECT.BUTTERFLY3        -> this.updateButterfly(cell.p, DIR.RIGHT);
                    OBJECT.BUTTERFLY4        -> this.updateButterfly(cell.p, DIR.DOWN);
                    OBJECT.EXPLODETOSPACE0   -> this.updateExplodeToSpace(cell.p, 0);
                    OBJECT.EXPLODETOSPACE1   -> this.updateExplodeToSpace(cell.p, 1);
                    OBJECT.EXPLODETOSPACE2   -> this.updateExplodeToSpace(cell.p, 2);
                    OBJECT.EXPLODETOSPACE3   -> this.updateExplodeToSpace(cell.p, 3);
                    OBJECT.EXPLODETOSPACE4   -> this.updateExplodeToSpace(cell.p, 4);
                    OBJECT.EXPLODETODIAMOND0 -> this.updateExplodeToDiamond(cell.p, 0);
                    OBJECT.EXPLODETODIAMOND1 -> this.updateExplodeToDiamond(cell.p, 1);
                    OBJECT.EXPLODETODIAMOND2 -> this.updateExplodeToDiamond(cell.p, 2);
                    OBJECT.EXPLODETODIAMOND3 -> this.updateExplodeToDiamond(cell.p, 3);
                    OBJECT.EXPLODETODIAMOND4 -> this.updateExplodeToDiamond(cell.p, 4);
                    OBJECT.AMOEBA            -> this.updateAmoeba(cell.p);
                    OBJECT.PREOUTBOX         -> this.updatePreOutbox(cell.p);
                    OBJECT.SPACE,
                    OBJECT.DIRT,
                    OBJECT.BRICKWALL,
                    OBJECT.MAGICWALL,
                    OBJECT.OUTBOX,
                    OBJECT.STEELWALL         -> Unit
                }
            }
        }
        this.endFrame();
    }

    fun decreaseTimer(n: Int? = null): Boolean {
        this.timer = 0.coerceAtLeast(this.timer - (n ?: 1));
        this.publish("timer", this.timer);
        return (this.timer == 0);
    }

    fun autoDecreaseTimer() {
        if ((this.frame > this.birth) && ((this.frame % this.fps) == 0))
            this.decreaseTimer(1);
    }

    fun runOutTimer() {
        val amount = 3.coerceAtMost(this.timer);
        this.increaseScore(amount);
        if (this.decreaseTimer(amount))
            this.next();
    }

    fun collectDiamond() {
        this.diamonds.collected++;
        this.increaseScore(if (this.diamonds.collected > this.diamonds.needed) this.diamonds.extra else this.diamonds.value);
        this.publish("diamond", this.diamonds);
    }

    fun increaseScore(n: Int) {
        this.score += n;
        this.publish("score", this.score);
    }

    fun flashWhenEnoughDiamondsCollected() {
        if (this.flash == 0 && (this.diamonds.collected >= this.diamonds.needed))
            this.flash = this.frame + (this.fps / 5.0).roundToInt(); // flash for 1/5th of a second
        if (this.frame <= this.flash)
            this.publish("flash");
    }

    fun loseLevel() {
        this.reset();
    }

    fun winLevel() {
        this.won = true;
    }

    fun beginFrame() {
        this.frame++;
        this.amoeba.size = 0;
        this.amoeba.enclosed = true;
        this.idle = if (moving.dir != DIR.NONE) Idle()
        else Idle(
            blink = if (randomInt(1, 4) == 1) !this.idle.blink else this.idle.blink,
            tap = if (randomInt(1, 16) == 1) !this.idle.tap else this.idle.tap,
        )
    }

    fun endFrame() {
        if (this.amoeba.dead == null) {
            if (this.amoeba.enclosed)
                this.amoeba.dead = OBJECT.DIAMOND;
            else if (this.amoeba.size > this.amoeba.max)
                this.amoeba.dead = OBJECT.BOULDER;
            else if (this.amoeba.slow > 0)
                this.amoeba.slow--;
        }
        this.magic.active = this.magic.active && (--this.magic.time > 0);
        this.flashWhenEnoughDiamondsCollected();
        if (this.won)
            this.runOutTimer();
        else if (this.frame - this.foundRockford > (4 * this.fps))
            this.loseLevel();
        else
            this.autoDecreaseTimer();
    }

    fun updatePreRockford(p: BdPoint, n: Int) {
        if (this.frame >= this.birth)
            this.set(p, PREROCKFORDS[n + 1]);
    }

    fun updatePreOutbox(p: BdPoint) {
        if (this.diamonds.collected >= this.diamonds.needed)
            this.set(p, OBJECT.OUTBOX);
    }

    fun updateRockford(p: BdPoint, dir: DIR) {
        this.foundRockford = this.frame;
        if (this.won) {
            // do nothing -  don't let rockford move if he already found the outbox
        }
        else if (this.timer == 0) {
            this.explode(p);
        }
        else if (moving.grab) {
            if (this.isdirt(p, dir)) {
                this.clear(p, dir);
            }
            else if (this.isdiamond(p, dir) || this.isfallingdiamond(p, dir)) {
                this.clear(p, dir);
                this.collectDiamond();
            }
            else if (horizontal(dir) && this.isboulder(p, dir)) {
                this.push(p, dir);
            }
        }
        else if (this.isempty(p, dir) || this.isdirt(p, dir)) {
            this.move(p, dir, OBJECT.ROCKFORD);
        }
        else if (this.isdiamond(p, dir)) {
            this.move(p, dir, OBJECT.ROCKFORD);
            this.collectDiamond();
        }
        else if (horizontal(dir) && this.isboulder(p, dir)) {
            this.push(p, dir);
        }
        else if (this.isoutbox(p, dir)) {
            this.move(p, dir, OBJECT.ROCKFORD);
            this.winLevel();
        }
    }

    fun updateBoulder(p: BdPoint) {
        if (this.isempty(p, DIR.DOWN))
            this.set(p, OBJECT.BOULDERFALLING);
        else if (this.isrounded(p, DIR.DOWN) && this.isempty(p, DIR.LEFT) && this.isempty(p, DIR.DOWNLEFT))
            this.move(p, DIR.LEFT, OBJECT.BOULDERFALLING);
        else if (this.isrounded(p, DIR.DOWN) && this.isempty(p, DIR.RIGHT) && this.isempty(p, DIR.DOWNRIGHT))
            this.move(p, DIR.RIGHT, OBJECT.BOULDERFALLING);
    }

    fun updateBoulderFalling(p: BdPoint) {
        if (this.isempty(p, DIR.DOWN))
            this.move(p, DIR.DOWN, OBJECT.BOULDERFALLING);
        else if (this.isexplodable(p, DIR.DOWN))
            this.explode(p, DIR.DOWN);
        else if (this.ismagic(p, DIR.DOWN))
            this.domagic(p, OBJECT.DIAMOND);
        else if (this.isrounded(p, DIR.DOWN) && this.isempty(p, DIR.LEFT) && this.isempty(p, DIR.DOWNLEFT))
            this.move(p, DIR.LEFT, OBJECT.BOULDERFALLING);
        else if (this.isrounded(p, DIR.DOWN) && this.isempty(p, DIR.RIGHT) && this.isempty(p, DIR.DOWNRIGHT))
            this.move(p, DIR.RIGHT, OBJECT.BOULDERFALLING);
        else
            this.set(p, OBJECT.BOULDER);
    }

    fun updateDiamond(p: BdPoint) {
        if (this.isempty(p, DIR.DOWN))
            this.set(p, OBJECT.DIAMONDFALLING);
        else if (this.isrounded(p, DIR.DOWN) && this.isempty(p, DIR.LEFT) && this.isempty(p, DIR.DOWNLEFT))
            this.move(p, DIR.LEFT, OBJECT.DIAMONDFALLING);
        else if (this.isrounded(p, DIR.DOWN) && this.isempty(p, DIR.RIGHT) && this.isempty(p, DIR.DOWNRIGHT))
            this.move(p, DIR.RIGHT, OBJECT.DIAMONDFALLING);
    }

    fun updateDiamondFalling(p: BdPoint) {
        if (this.isempty(p, DIR.DOWN))
            this.move(p, DIR.DOWN, OBJECT.DIAMONDFALLING);
        else if (this.isexplodable(p, DIR.DOWN))
            this.explode(p, DIR.DOWN);
        else if (this.ismagic(p, DIR.DOWN))
            this.domagic(p, OBJECT.BOULDER);
        else if (this.isrounded(p, DIR.DOWN) && this.isempty(p, DIR.LEFT) && this.isempty(p, DIR.DOWNLEFT))
            this.move(p, DIR.LEFT, OBJECT.DIAMONDFALLING);
        else if (this.isrounded(p, DIR.DOWN) && this.isempty(p, DIR.RIGHT) && this.isempty(p, DIR.DOWNRIGHT))
            this.move(p, DIR.RIGHT, OBJECT.DIAMONDFALLING);
        else
            this.set(p, OBJECT.DIAMOND);
    }

    fun updateFirefly(p: BdPoint, dir: DIR) {
        val newdir = rotateLeft(dir.ordinal);
        if (this.isrockford(p, DIR.UP) || this.isrockford(p, DIR.DOWN) || this.isrockford(p, DIR.LEFT) || this.isrockford(p, DIR.RIGHT))
            this.explode(p);
        else if (this.isamoeba(p, DIR.UP) || this.isamoeba(p, DIR.DOWN) || this.isamoeba(p, DIR.LEFT) || this.isamoeba(p, DIR.RIGHT))
            this.explode(p);
        else if (this.isempty(p, newdir))
            this.move(p, newdir, FIREFLIES[newdir]!!);
        else if (this.isempty(p, dir))
            this.move(p, dir.ordinal, FIREFLIES[dir.ordinal]!!);
        else
            this.set(p, FIREFLIES[rotateRight(dir.ordinal)]!!);
    }

    fun updateButterfly(p: BdPoint, dir: DIR) {
        val newdir = rotateRight(dir.ordinal);
        if (this.isrockford(p, DIR.UP) || this.isrockford(p, DIR.DOWN) || this.isrockford(p, DIR.LEFT) || this.isrockford(p, DIR.RIGHT))
            this.explode(p);
        else if (this.isamoeba(p, DIR.UP) || this.isamoeba(p, DIR.DOWN) || this.isamoeba(p, DIR.LEFT) || this.isamoeba(p, DIR.RIGHT))
            this.explode(p);
        else if (this.isempty(p, newdir))
            this.move(p, newdir, BUTTERFLIES[newdir]!!);
        else if (this.isempty(p, dir))
            this.move(p, dir.ordinal, BUTTERFLIES[dir.ordinal]!!);
        else
            this.set(p, BUTTERFLIES[rotateLeft(dir.ordinal)]!!);
    }

    fun updateExplodeToSpace(p: BdPoint, n: Int) = set(p, EXPLODETOSPACE[n + 1])
    fun updateExplodeToDiamond(p: BdPoint, n: Int) = set(p, EXPLODETODIAMOND[n + 1])

    fun updateAmoeba(p: BdPoint) {
        if (amoeba.dead != null) {
            set(p, this.amoeba.dead!!)
        }
        else {
            this.amoeba.size++
            if (this.isempty(p, DIR.UP) || this.isempty(p, DIR.DOWN) || this.isempty(p, DIR.RIGHT) || this.isempty(p, DIR.LEFT) ||
                this.isdirt(p, DIR.UP) || this.isdirt(p, DIR.DOWN) || this.isdirt(p, DIR.RIGHT) || this.isdirt(p, DIR.LEFT)) {
                this.amoeba.enclosed = false
            }
            if (this.frame >= this.birth) {
                val grow = if (this.amoeba.slow > 0) (randomInt(1, 128) < 4) else (randomInt(1, 4) == 1)
                val dir = randomChoice(arrayOf(DIR.UP, DIR.DOWN, DIR.LEFT, DIR.RIGHT))
                if (grow && (this.isdirt(p, dir) || this.isempty(p, dir)))
                    this.set(p, OBJECT.AMOEBA, dir.ordinal)
            }
        }
    }

    fun explode(p: BdPoint, dir: DIR) = explode(p, dir.ordinal)
    fun explode(p: BdPoint, dir: Int = DIR.NONE.ordinal) {
        val p2 = Point(p.x, p.y, dir)
        val explosion = if (isbutterfly(p2)) OBJECT.EXPLODETODIAMOND0 else OBJECT.EXPLODETOSPACE0
        set(p2, explosion)
        for (d in 0..7) {
            if (isexplodable(p2, d))
                explode(p2, d)
            else if (isconsumable(p2, d))
                set(p2, explosion, d)
        }
    }

    fun push(p: BdPoint, dir: DIR) = push(p, dir.ordinal)
    fun push(p: BdPoint, dir: Int) {
        val p2 = Point(p.x, p.y, dir)
        if (isempty(p2, dir)) {
            if (randomInt(1, 8) == 1) {
                move(p2, dir, OBJECT.BOULDER)
                if (!moving.grab)
                    move(p, dir, OBJECT.ROCKFORD)
            }
        }
    }

    fun domagic(p: BdPoint, to: OBJECT) {
        if (magic.time > 0) {
            magic.active = true
            clear(p)
            val p2 = BdPoint(p.x, p.y + 2)
            if (isempty(p2)) set(p2, to)
        }
    }

    private val subscriptions = mutableMapOf<String, MutableList<(Any?) -> Unit>>()

    fun subscribe(event: String, callback: (Any?) -> Unit) {
        subscriptions.getOrPut(event) { mutableListOf() }.add(callback)
    }

    fun publish(event: String, arg: Any? = null) {
        subscriptions[event]?.forEach { it(arg) }
    }
}

//=========================================================================
// GAME RENDERING
//=========================================================================

class Render(val game: Game) {

    init {
        game.subscribe("level") { this.onChangeLevel(it as Cave) }
        game.subscribe("score") { this.invalidateScore() }
        game.subscribe("timer") { this.invalidateScore() }
        game.subscribe("flash") { this.invalidateCave() }
        game.subscribe("cell") { this.invalidateCell(it as BdCell) }
    }

    lateinit var canvas: Graphics
    lateinit var ctx: Graphics
    lateinit var sprites: ImageResource
    val fps = 30
    val step = 1.0 / this.fps
    var frame = 0

    fun reset(graphics: Graphics, sprites: ImageResource) {
        this.canvas = graphics
        this.ctx = this.canvas//.getContext('2d');
        this.sprites = sprites;
        this.frame = 0;
//        this.ctxSprites = document.createElement('canvas').getContext('2d');
//        this.ctxSprites.canvas.width = this.sprites.width;
//        this.ctxSprites.canvas.height = this.sprites.height;
//        this.ctxSprites.drawImage(this.sprites, 0, 0, this.sprites.width, this.sprites.height, 0, 0, this.sprites.width, this.sprites.height);
        this.resize();
    }

    fun onChangeLevel(cave: Cave) {
        this.description(cave.description);
        this.colors(cave.color1, cave.color2);
        this.invalidateCave();
        this.invalidateScore();
//        Dom.disable('prev', cave.index === 0);
//        Dom.disable('next', cave.index === CAVES.length - 1);
    }

    data class Invalid(var score: Boolean = true, var cave: Boolean = true)

    val invalid = Invalid()
    fun invalidateScore() = also { this.invalid.score = true; }
    fun invalidateCave() = also { this.invalid.cave = true; }
    fun invalidateCell(cell: BdCell) = also { cell.invalid = true; }
    fun validateScore() = also { this.invalid.score = false; }
    fun validateCave() = also { this.invalid.cave = false; }
    fun validateCell(cell: BdCell) = also { cell.invalid = false; }

    fun update() {
        this.frame++;
        this.score();
        game.eachCell { cell(it) }
        this.validateCave();
    }

    fun score() {
        if (this.invalid.score) {
            //this.ctx.fillStyle='black';
            this.ctx.setColor(1f, 0f, 0f, 0f)
            this.ctx.fillRect(0, 0, this.canvas.width, this.dy);
            this.number(3, game.diamonds.needed, 2, true);
            this.letter(5, '$');
            this.number(6, if (game.diamonds.collected >= game.diamonds.needed) game.diamonds.extra else game.diamonds.value, 2);
            this.number(12, game.diamonds.collected, 2, true);
            this.number(25, game.timer, 3);
            this.number(31, game.score, 6);
            this.validateScore();
        }
    }

    fun number(x: Int, n: Int, width: Int, yellow: Boolean = false) {
        val word = ("000000$n").takeLast(width)//slice(-(width||2));
        for (i in word.indices)
            this.letter(x + i, word[i], yellow);
    }

    fun letter(x: Int, c: Char, yellow: Boolean = false) {
        // auto-scaling here from 32/32 to dx/dy can be slow... we should optimize and precatch rendered sprites at exact cell size (dx,dy)
        //this.ctx.drawImage(this.ctxSprites.canvas, (yellow ? 9 : 8) * 32, (c.charCodeAt(0)-32) * 16, 32, 16, (x*this.dx), 0, this.dx, this.dy-4);
        val src = Rectangle((if (yellow) 9 else 8) * 32, (c.code - 32) * 16, 32, 16)
        val dst = Rectangle(x * this.dx, 0, this.dx, this.dy - 4)
        this.ctx.drawImage(sprites, src, dst)
    }

    fun cell(cell: BdCell) {
        val o = cell.obj
        var sprite = o.sprite;
        if (this.invalid.cave || cell.invalid || (sprite.f > 1) || (o == OBJECT.ROCKFORD)) {
            if (o === OBJECT.ROCKFORD)
                return this.rockford(cell);
            else if ((o == OBJECT.SPACE) && (game.flash > game.frame))
                sprite = OBJECT.SPACE.flash!!;
            else if ((o == OBJECT.MAGICWALL) && !game.magic.active)
                sprite = OBJECT.BRICKWALL.sprite;
            this.sprite(sprite, cell);
            this.validateCell(cell);
        }
    }

    fun sprite(sprite: BdSprite, cell: BdCell) {
        val f = if (sprite.f > 0) (floor((sprite.fps * 1.0 / this.fps) * this.frame).roundToInt() % sprite.f) else 0
        // auto-scaling here from 32/32 to dx/dy can be slow... we should optimize and precatch rendered sprites at exact cell size (dx,dy)
        //this.ctx.drawImage(this.ctxSprites.canvas, (sprite.x + f) * 32, sprite.y * 32, 32, 32, cell.p.x * this.dx, (1+cell.p.y) * this.dy, this.dx, this.dy);
        val src = Rectangle((sprite.x + f) * 32, sprite.y * 32, 32, 32)
        val dst = Rectangle(cell.p.x * this.dx, canvas.height - (1 + cell.p.y) * this.dy, this.dx, this.dy)
        this.ctx.drawImage(sprites, src, dst)
    }

    fun rockford(cell: BdCell) {
        if ((moving.dir == DIR.LEFT) || (vertical(moving.dir) && (moving.lastXDir == DIR.LEFT)))
            this.sprite(OBJECT.ROCKFORD.left!!, cell);
        else if ((moving.dir == DIR.RIGHT) || (vertical(moving.dir) && (moving.lastXDir == DIR.RIGHT)))
            this.sprite(OBJECT.ROCKFORD.right!!, cell);
        else if (game.idle.blink && !game.idle.tap)
            this.sprite(OBJECT.ROCKFORD.blink!!, cell);
        else if (!game.idle.blink && game.idle.tap)
            this.sprite(OBJECT.ROCKFORD.tap!!, cell);
        else if (game.idle.blink && game.idle.tap)
            this.sprite(OBJECT.ROCKFORD.blinktap!!, cell);
        else
            this.sprite(OBJECT.ROCKFORD.sprite, cell);
    }

    fun description(msg: String) {
        Log.info(msg);//Dom.set('description', msg);
    }

    private lateinit var pixmap: PixelData

    fun colors(color1: Int, color2: Int) {
        if (!::pixmap.isInitialized) pixmap = sprites.pixelData()

        val pixels = pixmap.cloned()
        for (y in 0 until this.sprites.height) {
            for (x in 0 until this.sprites.width) {
                val color = pixels.rgb(x, y)
                if (color == 0x3F3F3F) { // mostly the metalic wall
                    pixels.rgb(x, y, color2)
                }
                if (color == 0xA52A00) { // mostly the dirt
                    pixels.rgb(x, y, color1)
                }
            }
        }

        // replace our sprites
        sprites.dispose()
        sprites = pixels.imageResource()
    }

    var dx = 32
    var dy = 32

    fun resize() {
        val visibleArea = Size(40, 23);            // 40x22 + 1 row for score at top - TODO: scrollable area
//        this.canvas.width  = this.canvas.clientWidth;  // set canvas logical size equal to its physical size
//        this.canvas.height = this.canvas.clientHeight; // (ditto)
        this.dx = this.canvas.width / visibleArea.width;  // calculate pixel size of a single game cell
        this.dy = this.canvas.height / visibleArea.height;  // (ditto)
        Log.warn("dx $dx dy $dy")
        Log.warn("dx $dx dy $dy")
        Log.warn("dx $dx dy $dy")
        this.invalidateScore();
        this.invalidateCave();
    }
}

//=========================================================================
// GAME LOOP
//=========================================================================

val game = Game
val render = Render(game)

//var now = 0L
//var last = timestamp()
//var dt = 0.0
var gdt = 0.0
var rdt = 0.0;

// split to accommodate the engine api
//fun frame() {
//    now = timestamp();
//    dt = (now - last) / 1000.0; // using requestAnimationFrame have to be able to handle large delta's caused when it 'hibernates' in a background or non-visible tab
//    gdt += dt;
//    while (gdt > game.step) {
//        gdt -= game.step;
//        game.update();
//    }
//    rdt += dt;
//    if (rdt > render.step) {
//        rdt -= render.step;
//        render.update();
//    }
//    last = now;
//}

fun game_frame(dt: Float) {
    gdt += dt;
    while (gdt > game.step) {
        gdt -= game.step;
        game.update();
    }
}

fun render_frame(dt: Float) {
    rdt += dt;
    if (rdt > render.step) {
        rdt -= render.step;
        render.update();
    }
}

fun IntensiScreen.load(graphics: Graphics, sprites: ImageResource) {
    Log.warn("load")
    render.reset(graphics, sprites); // reset the canvas renderer with the loaded sprites <IMG>
    game.reset();          // reset the game
    addEvents();           // attach keydown and resize event handlers
    //frame();               //  ... and start the first frame !
};

fun IntensiScreen.addEvents() {
    keydown()
    keyup()
}

// for key handling this was easier...

fun IntensiScreen.keydown() {
    whenKeyDown(Input.Keys.UP) { moving.startUp() }
    whenKeyDown(Input.Keys.DOWN) { moving.startDown() }
    whenKeyDown(Input.Keys.LEFT) { moving.startLeft() }
    whenKeyDown(Input.Keys.RIGHT) { moving.startRight() }
    whenKeyDown(Input.Keys.R) { game.reset() }
    whenKeyDown(Input.Keys.PAGE_UP) { game.prev() }
    whenKeyDown(Input.Keys.PAGE_DOWN) { game.next() }
    whenKeyDown(Input.Keys.SPACE) { moving.startGrab() }
}

fun IntensiScreen.keyup() {
    whenKeyUp(Input.Keys.UP) { moving.stopUp() }
    whenKeyUp(Input.Keys.DOWN) { moving.stopDown() }
    whenKeyUp(Input.Keys.LEFT) { moving.stopLeft() }
    whenKeyUp(Input.Keys.RIGHT) { moving.stopRight() }
    whenKeyUp(Input.Keys.SPACE) { moving.stopGrab() }
}

object Moving {
    var dir = DIR.NONE
    var lastXDir = DIR.NONE
    var up = false
    var down = false
    var left = false
    var right = false
    var grab = false
    val startUp = { this.up = true; this.dir = DIR.UP; }
    val startDown = { this.down = true; this.dir = DIR.DOWN; }
    val startLeft = { this.left = true; this.dir = DIR.LEFT; this.lastXDir = DIR.LEFT; }
    val startRight = { this.right = true; this.dir = DIR.RIGHT; this.lastXDir = DIR.RIGHT; }
    val startGrab = { this.grab = true; }
    val stopUp = { this.up = false; this.dir = if (this.dir == DIR.UP) this.where() else this.dir; }
    val stopDown = { this.down = false; this.dir = if (this.dir == DIR.DOWN) this.where() else this.dir; }
    val stopLeft = { this.left = false; this.dir = if (this.dir == DIR.LEFT) this.where() else this.dir; }
    val stopRight = { this.right = false; this.dir = if (this.dir == DIR.RIGHT) this.where() else this.dir; }
    val stopGrab = { this.grab = false; }
    fun where(): DIR = when {
        this.up    -> DIR.UP
        this.down  -> DIR.DOWN
        this.left  -> DIR.LEFT
        this.right -> DIR.RIGHT
        else       -> DIR.NONE
    }
}

val moving = Moving

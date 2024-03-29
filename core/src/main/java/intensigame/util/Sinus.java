package intensigame.util;

public final class Sinus
    {
    public final static int SIN_TABLE_SIZE = 1024;

    public final static int SIN_TABLE_SIZE_MASK = SIN_TABLE_SIZE - 1;

    public final static int SIN_TABLE_SHIFT = 16;

    public final static int SIN_TABLE_MULTIPLIER = 1 << SIN_TABLE_SHIFT;

    public final static int COSINUS_OFFSET = SIN_TABLE_SIZE >> 2;



    public static Sinus instance()
        {
        if ( iInstance == null ) iInstance = new Sinus();
        return iInstance;
        }

    public final int sin( final int aIndex, final int aMultiplier )
        {
        return ( mySinTable[ aIndex & SIN_TABLE_SIZE_MASK ] * aMultiplier ) >> SIN_TABLE_SHIFT;
        }

    public final int cos( final int aIndex, final int aMultiplier )
        {
        return ( mySinTable[ ( aIndex + COSINUS_OFFSET ) & SIN_TABLE_SIZE_MASK ] * aMultiplier ) >> SIN_TABLE_SHIFT;
        }

    // Implementation

    private Sinus()
        {
        final int quarterSize = SIN_TABLE_SIZE / 4;
        for ( int idx = 0; idx < quarterSize; idx++ )
            {
            mySinTable[ idx ] = QUATER_TABLE[ idx ];
            mySinTable[ quarterSize * 2 - idx - 1 ] = QUATER_TABLE[ idx ];
            mySinTable[ quarterSize * 2 + idx ] = -QUATER_TABLE[ idx ];
            mySinTable[ quarterSize * 4 - idx - 1 ] = -QUATER_TABLE[ idx ];
            }
        }



    private int[] mySinTable = new int[SIN_TABLE_SIZE];

    private static Sinus iInstance;

    private static final int[] QUATER_TABLE = new int[]
            { 0, 402, 804, 1206, 1608, 2010, 2412, 2814, 3215, 3617, 4018, 4420, 4821, 5222, 5622, 6023,
              6423, 6823, 7223, 7623, 8022, 8421, 8819, 9218, 9616, 10013, 10410, 10807, 11204, 11600, 11995, 12390,
              12785, 13179, 13573, 13966, 14359, 14751, 15142, 15533, 15923, 16313, 16702, 17091, 17479, 17866, 18253, 18638,
              19024, 19408, 19792, 20175, 20557, 20938, 21319, 21699, 22078, 22456, 22833, 23210, 23586, 23960, 24334, 24707,
              25079, 25450, 25820, 26189, 26557, 26925, 27291, 27656, 28020, 28383, 28745, 29105, 29465, 29824, 30181, 30538,
              30893, 31247, 31600, 31952, 32302, 32651, 32999, 33346, 33692, 34036, 34379, 34721, 35061, 35400, 35738, 36074,
              36409, 36743, 37075, 37406, 37736, 38064, 38390, 38716, 39039, 39362, 39682, 40002, 40319, 40636, 40950, 41263,
              41575, 41885, 42194, 42501, 42806, 43110, 43412, 43712, 44011, 44308, 44603, 44897, 45189, 45480, 45768, 46055,
              46340, 46624, 46906, 47186, 47464, 47740, 48015, 48288, 48558, 48828, 49095, 49360, 49624, 49886, 50146, 50403,
              50660, 50914, 51166, 51416, 51665, 51911, 52155, 52398, 52639, 52877, 53114, 53348, 53581, 53811, 54040, 54266,
              54491, 54713, 54933, 55152, 55368, 55582, 55794, 56004, 56212, 56417, 56621, 56822, 57022, 57219, 57414, 57606,
              57797, 57986, 58172, 58356, 58538, 58718, 58895, 59070, 59243, 59414, 59583, 59749, 59913, 60075, 60235, 60392,
              60547, 60700, 60850, 60998, 61144, 61288, 61429, 61568, 61705, 61839, 61971, 62100, 62228, 62353, 62475, 62596,
              62714, 62829, 62942, 63053, 63162, 63268, 63371, 63473, 63571, 63668, 63762, 63854, 63943, 64030, 64115, 64197,
              64276, 64353, 64428, 64501, 64571, 64638, 64703, 64766, 64826, 64884, 64939, 64992, 65043, 65091, 65136, 65179,
              65220, 65258, 65294, 65327, 65358, 65386, 65412, 65436, 65457, 65475, 65491, 65505, 65516, 65524, 65531, 65534,
            };
    }

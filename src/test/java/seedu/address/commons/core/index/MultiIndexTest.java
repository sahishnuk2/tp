package seedu.address.commons.core.index;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

public class MultiIndexTest {

    @Test
    public void constructor_validSingle_success() {
        Index index = Index.fromOneBased(3);
        MultiIndex multi = new MultiIndex(index);
        assertEquals(index, multi.getLowerBound());
        assertEquals(index, multi.getUpperBound());
        assertTrue(multi.isSingle());
    }

    @Test
    public void constructor_validRange_success() {
        Index lower = Index.fromOneBased(2);
        Index upper = Index.fromOneBased(5);
        MultiIndex multi = new MultiIndex(lower, upper);

        assertEquals(lower, multi.getLowerBound());
        assertEquals(upper, multi.getUpperBound());
        assertFalse(multi.isSingle());
    }

    @Test
    public void constructor_lowerGreaterThanUpper_throwsIllegalArgumentException() {
        Index lower = Index.fromOneBased(5);
        Index upper = Index.fromOneBased(2);
        assertThrows(IllegalArgumentException.class, () -> new MultiIndex(lower, upper));
    }

    @Test
    public void constructor_nullArguments_throwsNullPointerException() {
        Index valid = Index.fromOneBased(1);
        assertThrows(NullPointerException.class, () -> new MultiIndex(null, valid));
        assertThrows(NullPointerException.class, () -> new MultiIndex(valid, null));
        assertThrows(NullPointerException.class, () -> new MultiIndex((Index) null));
    }

    @Test
    public void toIndexList_singleIndex_success() {
        MultiIndex multi = new MultiIndex(Index.fromOneBased(4));
        List<Index> list = multi.toIndexList();
        assertEquals(1, list.size());
        assertEquals(Index.fromOneBased(4), list.get(0));
    }

    @Test
    public void toIndexList_range_success() {
        MultiIndex multi = new MultiIndex(Index.fromOneBased(2), Index.fromOneBased(4));
        List<Index> list = multi.toIndexList();
        assertEquals(List.of(Index.fromOneBased(2), Index.fromOneBased(3), Index.fromOneBased(4)), list);
    }

    @Test
    public void toString_singleAndRange_success() {
        MultiIndex single = new MultiIndex(Index.fromOneBased(3));
        MultiIndex range = new MultiIndex(Index.fromOneBased(1), Index.fromOneBased(3));

        assertEquals("3", single.toString());
        assertEquals("1:3", range.toString());
    }

    @Test
    public void equals_sameObject_true() {
        MultiIndex multi = new MultiIndex(Index.fromOneBased(1), Index.fromOneBased(2));
        assertTrue(multi.equals(multi));
    }

    @Test
    public void equals_differentType_false() {
        MultiIndex multi = new MultiIndex(Index.fromOneBased(1), Index.fromOneBased(2));
        assertFalse(multi.equals("notAMultiIndex"));
    }

    @Test
    public void equals_sameValues_true() {
        MultiIndex a = new MultiIndex(Index.fromOneBased(1), Index.fromOneBased(3));
        MultiIndex b = new MultiIndex(Index.fromOneBased(1), Index.fromOneBased(3));
        assertTrue(a.equals(b));
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_differentValues_false() {
        Index one = Index.fromOneBased(1);
        Index two = Index.fromOneBased(2);
        assertNotEquals(one, two, "Indices should not be equal");
        MultiIndex a = new MultiIndex(one, Index.fromOneBased(3));
        MultiIndex b = new MultiIndex(two, Index.fromOneBased(3));
        assertFalse(a.equals(b));
    }

}

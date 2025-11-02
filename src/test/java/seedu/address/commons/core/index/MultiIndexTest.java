package seedu.address.commons.core.index;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class MultiIndexTest {

    @Test
    void constructor_validRange_success() {
        Index lower = Index.fromOneBased(1);
        Index upper = Index.fromOneBased(3);
        MultiIndex multiIndex = new MultiIndex(lower, upper);

        assertEquals(lower, multiIndex.getLowerBound(), "Lower bound should match");
        assertEquals(upper, multiIndex.getUpperBound(), "Upper bound should match");
    }

    @Test
    void constructor_singleIndex_success() {
        Index single = Index.fromOneBased(5);
        MultiIndex multiIndex = new MultiIndex(single);

        assertEquals(single, multiIndex.getLowerBound(), "Lower bound should match single index");
        assertEquals(single, multiIndex.getUpperBound(), "Upper bound should match single index");
        assertTrue(multiIndex.isSingle(), "Should represent a single index");
    }

    @Test
    void constructor_invalidRange_throwsIllegalArgumentException() {
        Index lower = Index.fromOneBased(5);
        Index upper = Index.fromOneBased(3);
        assertThrows(IllegalArgumentException.class, () -> new MultiIndex(lower, upper),
                "Lower bound greater than upper bound should throw");
    }

    @Test
    void toIndexList_validRange_correctListReturned() {
        MultiIndex multiIndex = new MultiIndex(Index.fromOneBased(2), Index.fromOneBased(4));
        List<Index> indices = multiIndex.toIndexList();

        assertEquals(3, indices.size(), "List should contain three indices");
        assertEquals(Index.fromOneBased(2), indices.get(0));
        assertEquals(Index.fromOneBased(3), indices.get(1));
        assertEquals(Index.fromOneBased(4), indices.get(2));
    }

    @Test
    void toIndexList_singleIndex_returnsSingleElementList() {
        MultiIndex multiIndex = new MultiIndex(Index.fromOneBased(7));
        List<Index> indices = multiIndex.toIndexList();

        assertEquals(1, indices.size(), "List should contain only one element");
        assertEquals(Index.fromOneBased(7), indices.get(0));
    }

    @Test
    void toString_range_correctFormat() {
        MultiIndex multiIndex = new MultiIndex(Index.fromOneBased(1), Index.fromOneBased(5));
        assertEquals("1:5", multiIndex.toString());
    }

    @Test
    void toString_singleIndex_correctFormat() {
        MultiIndex multiIndex = new MultiIndex(Index.fromOneBased(3));
        assertEquals("3", multiIndex.toString());
    }

    @Test
    void equals_sameBounds_true() {
        MultiIndex a = new MultiIndex(Index.fromOneBased(1), Index.fromOneBased(3));
        MultiIndex b = new MultiIndex(Index.fromOneBased(1), Index.fromOneBased(3));

        assertEquals(a, b, "MultiIndex with same bounds should be equal");
    }

    @Test
    void equals_differentBounds_false() {
        MultiIndex a = new MultiIndex(Index.fromOneBased(1), Index.fromOneBased(3));
        MultiIndex b = new MultiIndex(Index.fromOneBased(2), Index.fromOneBased(4));

        assertNotEquals(a, b, "Different ranges should not be equal");
    }
}

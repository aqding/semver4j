package org.semver4j;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static org.semver4j.Range.RangeOperator.*;

/**
 * The internal expression class used to create a ranges.<br>
 * Allows to create ranges using a fluent interface.
 * <p>
 * Usage:
 * <pre>
 * equal("1.0.0")
 *     .and(less("2.0.0"))
 *     .or(greaterOrEqual("3.0.0"))
 * </pre>
 * <p>
 * Will produce range:
 * <pre>
 * (=1.0.0 and &lt;2.0.0) or &gt;=3.0.0
 * </pre>
 *
 * @since 4.2.0
 */
@SuppressWarnings("checkstyle:DeclarationOrder")
public class RangesExpression {
    @NotNull
    private final RangesList rangesList = new RangesList();

    @NotNull
    private final List<@NotNull Range> andOperationRanges = new ArrayList<>();

    /**
     * Expression for equal range item.
     *
     * @param version should be a valid semver string
     */
    @NotNull
    public static RangesExpression equal(@NotNull final String version) {
        return equal(new Semver(version));
    }

    /**
     * Expression for equal range item.
     */
    @NotNull
    public static RangesExpression equal(@NotNull final Semver version) {
        return new RangesExpression(new Range(version, EQ));
    }

    /**
     * Expression for greater range item.
     *
     * @param version should be a valid semver string
     */
    @NotNull
    public static RangesExpression greater(@NotNull final String version) {
        return greater(new Semver(version));
    }

    /**
     * Expression for greater range item.
     */
    @NotNull
    public static RangesExpression greater(@NotNull final Semver version) {
        return new RangesExpression(new Range(version, GT));
    }

    /**
     * Expression for greater or equal range item.
     *
     * @param version should be a valid semver string
     */
    @NotNull
    public static RangesExpression greaterOrEqual(@NotNull final String version) {
        return greaterOrEqual(new Semver(version));
    }

    /**
     * Expression for greater or equal range item.
     */
    @NotNull
    public static RangesExpression greaterOrEqual(@NotNull final Semver version) {
        return new RangesExpression(new Range(version, GTE));
    }

    /**
     * Expression for less range item.
     *
     * @param version should be a valid semver string
     */
    @NotNull
    public static RangesExpression less(@NotNull final String version) {
        return less(new Semver(version));
    }

    /**
     * Expression for lee range item.
     */
    @NotNull
    public static RangesExpression less(@NotNull final Semver version) {
        return new RangesExpression(new Range(version, LT));
    }

    /**
     * Expression for less or equal range item.
     *
     * @param version should be a valid semver string
     */
    @NotNull
    public static RangesExpression lessOrEqual(@NotNull final String version) {
        return lessOrEqual(new Semver(version));
    }

    /**
     * Expression for less or equal range item.
     */
    @NotNull
    public static RangesExpression lessOrEqual(@NotNull final Semver version) {
        return new RangesExpression(new Range(version, LTE));
    }

    RangesExpression(@NotNull final Range range) {
        andOperationRanges.add(range);
    }

    /**
     * Allows to join ranges using {@code AND} operator.
     */
    @NotNull
    public RangesExpression and(@NotNull final RangesExpression rangeExpression) {
        RangesList rangesList = rangeExpression.get();
        List<List<Range>> lists = rangesList.get();
        for (List<Range> list : lists) {
            andOperationRanges.addAll(list);
            if (lists.size() > 1) {
                flushAndClearAndOperationRangesToRangesList();
            }
        }
        return this;
    }

    /**
     * Allows to join ranges using {@code OR} operator.
     */
    @NotNull
    public RangesExpression or(@NotNull final RangesExpression rangeExpression) {
        flushAndClearAndOperationRangesToRangesList();
        return and(rangeExpression);
    }

    @NotNull
    RangesList get() {
        if (!andOperationRanges.isEmpty()) {
            flushAndClearAndOperationRangesToRangesList();
        }
        return rangesList;
    }

    private void flushAndClearAndOperationRangesToRangesList() {
        rangesList.add(new ArrayList<>(andOperationRanges));
        andOperationRanges.clear();
    }
}

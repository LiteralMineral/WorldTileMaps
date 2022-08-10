//public class SphereEquation extends Equation {
//
//
//    private final static String description = "";
//
//
//
//    /**
//     * Equation for a sphere:   x^2 + y^2 + z^2 + t^2 (I'm using time to conceptualize this fourth dimension) = constant
//     *
//     * This will only give the positive portions of the sphere.
//     */
//
//
//
//    /**
//     * @param multipliers
//     * @param offsets
//     * @param radius
//     */
//    public SphereEquation(int[] multipliers, int[] offsets, int radius) {
//        super(multipliers, offsets, (int) Math.pow(radius, 2)); // in a sphere, the constant value is conceptualized as radius squared
//
//
//    }
//
//
//    /**
//     * Assumes the user won't feed it more values than is necessary
//     * @param knownValues
//     * @return
//     */
////    public int solveForNextTermsValue(int ... knownValues) {
////
////        // first process known values, then process unknown values, assuming them to be zero just so you can get the right values.
////
////        double sum = constraint; // set it so that sum = constant - (all the other terms)
////
////        int targetVariableIndex = Math.min(knownValues.length, coefficients.length - 1); // make it so an excess of variables won't break the code, but instead give wonky information.
////
////
////        for (int i = 0; i < targetVariableIndex ; i++) {
////
////            sum -= Math.pow(coefficients[0] * (knownValues[0] - center[0]), 2); // compute the value for the nth term and subtract it from sum.
////
////        }
////
////
////        for (int i = targetVariableIndex + 1; i < coefficients.length -1; i++) { // skip the target valueTHIS IS FINE BECAUSE A FOR LOOP CHECKS THE CONDITION FIRST.
////            sum -= Math.pow(coefficients[0] * (-center[0]), 2); // compute the value for the rest of the terms assuming the missing values are 0. Because if you haven't traversed them yet, they are 0.
////        }
////
////
////        double sumSquareRoot = ((int) sum >= 0) ? Math.sqrt(sum) : 0; // make the square root of sum if it will be real, otherwise use 0
////
////        return (int) ( ((1 / coefficients[targetVariableIndex]) * sumSquareRoot) + center[targetVariableIndex]); // add the offset for the variable you're trying to find. Which should be the variable juuuust beyond the last one provided.
////    }
//
//
//    @Override
//    public int calculatePoint(int ... coordinates) {
//        for ()
//
//    }
//
//
//    @Override
//    public boolean containsPoint(int ... coordinates) {
//
//
//
//        return false;
//    }
//
//
//    public String toString() {
//        return description;
//
//    }
//
//
//
//
//}
//

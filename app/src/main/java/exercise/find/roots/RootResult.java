package exercise.find.roots;

import java.io.Serializable;

public class RootResult implements Serializable {
    public final long _root1;
    public final long _root2;
    public transient final boolean _isValidRootFound;
    public final long _calculationTime;

    RootResult(long root1, long root2, boolean isValidRootFound, long calculationTime) {
        _root1 = root1;
        _root2 = root2;
        _isValidRootFound = isValidRootFound;
        _calculationTime = calculationTime;
    }
}
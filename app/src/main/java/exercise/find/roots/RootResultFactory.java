package exercise.find.roots;

public class RootResultFactory {
    private RootResultFactory(){}


    public static RootResult createRootResultFound(long root1, long root2, long calculationTime){
        return new RootResult(root1, root2,true,calculationTime);
    }

    public static RootResult createRootResultPrimeFound(long target, long calculationTime){
        return new RootResult(target, 1,true,calculationTime);
    }

    public static RootResult createRootResultForTargetZero(){
        return new RootResult(0,0,true,0);
    }



    public static RootResult createRootResultNotFound(long calculationTime){
        return new RootResult(-1,-1,false,calculationTime);
    }
}

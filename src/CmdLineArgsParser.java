import java.util.Dictionary;
import java.util.Hashtable;

public class CmdLineArgsParser {
    private Dictionary<String, String> argsDict = new Hashtable<>();

    public CmdLineArgsParser(String [] args){
        parseArgs(args);
    }

    public CmdLineArgsParser(String args) {
        parseArgs(args.split(" "));
    }

    public Dictionary<String, String> getArgsDict() {
        return argsDict;
    }

    private void parseArgs (String[] args){
        String argName = null;

        for (String arg : args){
            if (arg.startsWith("-") && arg.length() == 2 || arg.startsWith("--")){
                argName = arg;
            } else {
                if (argName != null){
                    argsDict.put(argName, arg);
                    argName = null;
                }
            }
        }
    }

    public String getArgumentValue(String shortName, String longName) {
        String value = argsDict.get(shortName);

        if (value == null) {
            value = argsDict.get(longName);
        }

        return value;
    }

}

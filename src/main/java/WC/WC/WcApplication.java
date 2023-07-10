package WC.WC;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;
@SpringBootApplication
@CommandLine.Command(name = "ccwc", mixinStandardHelpOptions = true,version = "ccwc 1.0", description = "counts")
public class WcApplication implements Callable<Result>{




	public static void main(String[] args) {
		var WC = new WcApplication();
		var cmd = new CommandLine(WC);
		System.out.println("here");
		var exitCode = cmd.execute(args);

		Result result = cmd.getExecutionResult();
		var resultString = new StringBuffer();
		resultString.append(" ");
		if (WC.countLine) {
			resultString.append(" ").append(result.countLines);
		}
		if (WC.countWord) {
			resultString.append(" ").append(result.countWords);
		}
		if (WC.countChar) {
			resultString.append(" ").append(result.countChars);
		}
		resultString.append(" ").append(WC.file.getName());
		System.out.println(resultString.toString());
//		SpringApplication.run(WcApplication.class, args);
		System.exit(exitCode);

	}
	@CommandLine.Option(names = {"-c"},description = "chars")
	private boolean countChar;

	@CommandLine.Option(names = {"-w"},description = "words")
	private boolean countWord;

	@CommandLine.Option(names = {"-l"},description = "lines")
	private boolean countLine;

	private boolean countAll;

	@CommandLine.Parameters(index = "0")
	private File file;


	@Override
	public Result call() throws Exception {
		System.out.println("here");
		Result result = new Result();
		var bytes = Files.readAllBytes(Path.of(this.file.toURI()));
		System.out.println(this.countLine);
		if((this.countChar == false && this.countWord) == (false && this.countLine == false))
		{
			this.countAll=true;
		}
		//this.countAll = (this.countChar == this.countLine)&&(this.countChar=this.countWord);
		System.out.println(this.countAll);
		if(this.countAll)
		{
			this.countChar=true;
			this.countLine=true;
			this.countWord=true;
		}
		if(this.countChar){
			result.countChars=bytes.length;
		}
		if(this.countLine){
			int i=0;
			for(byte a:bytes)
			{
				if(a == '\n'){
					i++;
				}
			}
			result.countLines =i;
		}
		if (this.countWord) {
			var i = 0;
			var lastWordCount = 0;
			for (byte byte2 : bytes) {
				if (Character.isWhitespace(byte2)) {
					if (lastWordCount > 0) {
						i++;
						lastWordCount = 0;
					}
				} else {
					lastWordCount++;
				}
			}
			if (lastWordCount > 0){
				i++;
			}
			result.countWords = i;
		}
		return result;
	}
}

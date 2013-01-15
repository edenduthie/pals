package pals.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.log4j.Logger;

import pals.Configuration;
import pals.Globals;
import pals.analysis.AnalysisException;
import pals.entity.Analysis;
import pals.entity.DataSet;
import pals.entity.DataSetVersion;
import pals.entity.ModelOutput;
import pals.entity.User;

public class PalsFileUtils
{
	private static Logger log = Logger.getLogger(PalsFileUtils.class);
	private static List<String> ERROR_CODES = Configuration.getInstance().ERROR_CODES;

	public static void copyFileInJava(String srcPath, String targetPath)
			throws IOException
	{
		log.info("Copying file " + srcPath + " to " + targetPath);
		FileInputStream in = null;
		FileOutputStream out = null;
		File userFile = new File(targetPath);
		File srcFile = new File(srcPath);
		try
		{
			in = new FileInputStream(srcFile);
			out = new FileOutputStream(userFile);
			int c;

			while ((c = in.read()) != -1)
			{
				out.write(c);
			}
		}
		finally
		{
			if (in != null)
			{
				in.close();
			}
			if (out != null)
			{
				out.close();
			}
		}
	}

	/**
	 * Executes a shell command using Apache Commons Exec.
	 * By setting the exit value, it throws an exception
	 * stderr only reports that the process exited with non-default value.
	 * That is not useful and the stdout contains the custom error
	 * message from  stop().
	 * Using R --vanilla --slave, the message from the R interactive
	 * mode can be suppressed and only the custom error message will
	 * be present.
	 * @param cmdStr
	 * @throws AnalysisException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void executeCommand(String cmdStr) throws AnalysisException,
			IOException, InterruptedException
	{
		log.info("Executing command: " + cmdStr);
		String cmd = findRScriptName(cmdStr);
		
		CommandLine commandLine = CommandLine.parse(cmdStr);
		DefaultExecutor executor = new DefaultExecutor();
		int normalExitVal = Configuration.getInstance().DEFAULT_EXIT_CODE;
		executor.setExitValue(normalExitVal);
		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		PumpStreamHandler psh = new PumpStreamHandler(stdout);
		executor.setStreamHandler(psh);
		//String errMsg = null;
		int exitValue = normalExitVal;
		// exitValue is not used to detect an error, because
		// setExitValue() makes execute() to throw an exception
		// when the exitValue is not the set value.
		try
		{
			exitValue = executor.execute(commandLine);
		}
		catch (ExecuteException e)
		{
			//errMsg = e.getMessage();
			//log.error(errMsg);
			throw new AnalysisException(parseOutputForError(stdout, cmd));
		}
		catch (IOException e)
		{
			//errMsg = e.getMessage();
			//log.error(errMsg);
			throw new IOException(e.getCause().getMessage());
		}
		finally
		{
			// Maybe the executor can clean up itself - e.g., stopping threads and closing streams
		}
	}

	/**
	 * Find what R command was run
	 * @param cmdStr
	 * @return
	 */
	private static String findRScriptName(String cmdStr)
	{
		String cmd = cmdStr;
		//String[] cmdList = cmdStr.split("\b");
		//Pattern p = Pattern.compile("\b*([^\b]*[.]{1}+R)\b.*$");
		Pattern p = Pattern.compile("([^/]*.R)");
		Matcher m = p.matcher(cmdStr);
		if (m.find())
			cmd = m.group(1);

		return cmd;
	}

	/**
	 * Filtering out the error message. 
	 * cmdStr is not used, because it is a part of
	 * the error message returned from R.
	 * If "Error" is not found in stdout, the whole
	 * stdout is returned.
	 * @param stdout
	 * @param cmd
	 * @return
	 * @throws IOException
	 */
	public static String parseOutputForError(ByteArrayOutputStream stdout,
			String cmd) throws IOException
	{
		String errMsg = stdout.toString(); // default return value
		
		ByteArrayInputStream in = new ByteArrayInputStream(stdout.toByteArray());
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		String foundErr = null;
		while ((line = reader.readLine()) != null)
		{
			// expecting only one Error line
			if (line.contains("Error"))
			{
				for (String code: ERROR_CODES)
				{
					if (line.contains(code))
					{
						foundErr = "Error: " + cmd + " (" + code + ")";
						// this could include description of the error code
						break;
					}
				}
				
				if(foundErr != null)
				{
					log.debug(line);
					break;
				}
			}
		}
		if(foundErr != null) 
		{
			return  foundErr;
		}
		else
		{
			return errMsg; 
		}
	}

	public static String getUserDirPath(User user)
	{
		return Configuration.getInstance().PATH_TO_APP_DATA + "/"
				+ user.getUsername();
	}

	private static String getModelOutputDirPath(Integer modelOutputId, User user)
	{
		// return getUserDirectory(userName) + File.separator +
		// Globals.MODEL_OUTPUT_DIR_PREFIX + modelOutputId;
		return getUserDirPath(user);
	}

	public static String getModelOutputDirPath(ModelOutput modelOutput)
	{
		return getModelOutputDirPath(modelOutput.getId(),
				modelOutput.getOwner());
	}

	public static String getModelOutputFilePathWithSuffix(
			Integer modelOutputId, User user, String suffix)
	{
		return getModelOutputDirPath(modelOutputId, user) + File.separator
				+ Globals.MODEL_OUTPUT_FILE_PREFIX + modelOutputId + suffix;
	}

	private static String getModelOutputFilePath(Integer modelOutputId,
			User user)
	{
		return getModelOutputFilePathWithSuffix(modelOutputId, user,
				Globals.NETCDF_FILE_SUFFIX);
	}

	public static String getModelOutputFilePath(ModelOutput modelOutput)
	{
		return getModelOutputFilePath(modelOutput.getId(),
				modelOutput.getOwner());
	}

	public static String getDataSetDirPath(DataSet dataSet)
	{
		// return Configuration.getInstance().PATH_TO_APP_DATA + File.separator
		// + dataSet.getUserName() + File.separator +
		// Globals.DATA_SET_DIR_PREFIX + dataSet.getDataSetId();
		return getUserDirPath(dataSet.getOwner());
	}

	public static String getDataSetVersionFilePathWidthSuffix(
			DataSetVersion dataSetVersion, String suffix)
	{
		return getDataSetDirPath(dataSetVersion.getDataSet()) + "/"
				+ Globals.DATA_SET_FILE_PREFIX
				+ dataSetVersion.getDataSet().getId() + "."
				+ dataSetVersion.getId() + suffix;
	}

	public static String getDataSetVersionFluxFilePath(
			DataSetVersion dataSetVersion)
	{
		return getDataSetVersionFilePathWidthSuffix(dataSetVersion, "_flux"
				+ Globals.NETCDF_FILE_SUFFIX);
	}

	public static String getDataSetVersionMetFilePath(
			DataSetVersion dataSetVersion)
	{
		return getDataSetVersionFilePathWidthSuffix(dataSetVersion, "_met"
				+ Globals.NETCDF_FILE_SUFFIX);
	}

	public static File getDataSetVersionMetFile(DataSetVersion dataSetVersion)
	{
		return new File(getDataSetVersionMetFilePath(dataSetVersion));
	}

	public static File getDataSetVersionFluxFile(DataSetVersion dataSetVersion)
	{
		return new File(getDataSetVersionFluxFilePath(dataSetVersion));
	}

	public static String getDataSetVersionBenchmarkFilePath(
			DataSetVersion dataSetVersion)
	{
		return getDataSetVersionFilePathWidthSuffix(dataSetVersion, "_bench"
				+ Globals.NETCDF_FILE_SUFFIX);
	}

	/*
	 * OLD METHOD
	 * 
	 * public static String getAnalysisRunFileLabel(AnalysisRun analRun) {
	 * String fileName = Globals.MODEL_OUTPUT_FILE_PREFIX +
	 * analRun.getModelOutputId() + Globals.ANALYSIS_RUN_FILE_PREFIX +
	 * analRun.getAnalysisId(); return
	 * getModelOutputDirPath(analRun.getModelOutputId(),
	 * analRun.getModelOutput().getUser()) + File.separator + fileName; }
	 */

	public static String getAnalysisRunFileLabel(Analysis analRun)
	{
		// String fileName = Globals.MODEL_OUTPUT_FILE_PREFIX +
		// analRun.getAnalysisRunId() + Globals.ANALYSIS_RUN_FILE_PREFIX +
		// analRun.getAnalysisId();
		String fileName = Globals.ANALYSIS_RUN_FILE_PREFIX + analRun.getId();
		return getUserDirPath(analRun.getAnalysable().getOwner())
				+ File.separator + fileName;
	}

	public static File getAnalysisRunFilePDF(Analysis analRun)
	{
		return new File(getAnalysisRunFileLabel(analRun)
				+ Globals.PDF_FILE_SUFFIX);
	}

	public static File getAnalysisRunFileBenchPDF(Analysis analRun)
	{
		return new File(getAnalysisRunFileLabelBench(analRun)
				+ Globals.PDF_FILE_SUFFIX);
	}

	public static File getAnalysisRunFilePNG(Analysis analRun)
	{
		return new File(getAnalysisRunFileLabel(analRun)
				+ Globals.PNG_FILE_SUFFIX);
	}

	public static File getAnalysisRunFileBenchPNG(Analysis analRun)
	{
		return new File(getAnalysisRunFileLabelBench(analRun)
				+ Globals.PNG_FILE_SUFFIX);
	}

	public static File getAnalysisRunFileThumb(Analysis analRun)
	{
		return new File(getAnalysisRunFileLabel(analRun)
				+ Globals.THUMB_FILE_SUFFIX);
	}

	public static String correctSlashesForR(String input)
	{
		if (!System.getProperty("file.separator").equals("/"))
		{
			input = input.replace('/', '\\');
		}
		String doubleBackslash = input.replace("\\", "\\\\");
		return doubleBackslash;
	}

	public static String getAnalysisRunFileLabelBench(Analysis analRun)
	{
		String fileName = Globals.ANALYSIS_RUN_FILE_PREFIX_BENCH
				+ analRun.getId();
		return getUserDirPath(analRun.getAnalysable().getOwner())
				+ File.separator + fileName;
	}

}

package br.com.sisped.util.mail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;

import com.outjected.email.api.TemplateProvider;
import com.outjected.email.api.TemplatingException;

public class VelocityTemplateUTF8 implements TemplateProvider {

	private VelocityEngine velocityEngine;
	private VelocityContext velocityContext;
	private InputStream inputStream;

	public VelocityTemplateUTF8(InputStream inputStream) {
		velocityEngine = new VelocityEngine();
		velocityEngine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
				"org.apache.velocity.runtime.log.NullLogChute");
		this.inputStream = inputStream;
	}

	public VelocityTemplateUTF8(String string) {
		this(new ByteArrayInputStream(string.getBytes()));
	}

	public VelocityTemplateUTF8(File file) throws FileNotFoundException {
		this(new FileInputStream(file));
	}

	public String merge(Map<String, Object> context) {
		StringWriter writer = new StringWriter();

		velocityContext = new VelocityContext(context);

		try {
			velocityEngine.evaluate(velocityContext, writer, "mailGenerated", new InputStreamReader(inputStream,
					"UTF-8"));
		} catch (ResourceNotFoundException e) {
			throw new TemplatingException("Unable to find template", e);
		} catch (ParseErrorException e) {
			throw new TemplatingException("Unable to find template", e);
		} catch (MethodInvocationException e) {
			throw new TemplatingException("Error processing method referenced in context", e);
		} catch (UnsupportedEncodingException e) {
			throw new TemplatingException("Unsupported encoding", e);
		}

		return writer.toString();
	}

}
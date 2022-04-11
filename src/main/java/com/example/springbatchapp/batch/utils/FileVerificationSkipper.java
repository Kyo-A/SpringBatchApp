package com.example.springbatchapp.batch.utils;

import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;

public class FileVerificationSkipper implements SkipPolicy {

    private static final Logger logger = LoggerFactory.getLogger("badRecordLogger");

    @Override
    public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
        if (t instanceof FileNotFoundException) {
            return false;
        } else if (t instanceof FlatFileParseException && skipCount <= 5) {
            FlatFileParseException ffpe = (FlatFileParseException) t;
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("An error occured while processing the " + ffpe.getLineNumber()
                    + " line of the file. Below was the faulty " + "input.\n");
            errorMessage.append(ffpe.getInput() + "\n");
            logger.error("{}", errorMessage.toString());
            return true;
        } else {
            return false;
        }
    }

}

package fr.midipascher.persistence.search;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collection;

import static org.apache.commons.io.FileUtils.listFiles;

/**
 * @author louis.gueye@gmail.com
 */
@Component
public class FileHelper {

    @SuppressWarnings("unchecked")
    public Collection<File> listFilesByFilter(File indexDirectory, String configFormat) {
        return listFiles(indexDirectory, new String[]{configFormat}, true);
    }

}

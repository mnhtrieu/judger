package eu.mnhtrieu.judge.Business.Storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class RawFile implements MultipartFile {

    private final InputStream source;
    private final String name;
    public RawFile(String source,String name) {
        this.source = new ByteArrayInputStream(source.replaceAll("\r","").getBytes(StandardCharsets.UTF_8));
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return name;
    }

    @Override
    public String getContentType() {
        return "text/plain";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return new byte[0];
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return source;
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException { }
}

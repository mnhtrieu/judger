package eu.mnhtrieu.judge.Business.Storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    private String location = "uploads";

    public String getLocation(){
        return location;
    }

    public void setLocation(String location){
        this.location = location;
    }

}

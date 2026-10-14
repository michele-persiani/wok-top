package it.unibo.msrehab.util;

import it.unibo.msrehab.config.Configuration;

import javax.servlet.http.HttpServletRequest;

public class PhotoManager
{
    private Configuration config = Configuration.getInstance();
    public static final String DEFAULT_PORTRAIT_FILENAME = "default-portrait.png";

    private static PhotoManager instance;

    public static PhotoManager getInstance()
    {
        if(instance == null)
            instance = new PhotoManager();
        return instance;
    }

    public String getPhotoFolder()
    {
        return config.getPhotosPath();
    }

    public String getPhotoUrl(HttpServletRequest request, String photoFileName)
    {
        String path = request.getContextPath();
        path += config.getPhotosPath();
        path += "/";
        path += photoFileName;
        return path;
    }
}

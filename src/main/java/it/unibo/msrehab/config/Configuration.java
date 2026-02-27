package it.unibo.msrehab.config;


import it.unibo.msrehab.model.entities.Exercise;
import it.unibo.msrehab.rl.ExerciseHelper;
import it.unibo.msrehab.rl.model.JPAController;

public class Configuration
{

    private String proxy;
    private String proxyPort;
    private String proxyUser;
    private String proxyPassword;
    private String dbUser;
    private String dbPassword;
    private String photosPath;
    private String mrReportsPath;
    private String cvssPath;
    private String dbPath;
    private String pddlPath;

    private static Configuration instance;


    public static Configuration getInstance()
    {
        if(instance == null)
        {
            ApplicationContextLoader l = new ApplicationContextLoader();
            l.load(Configuration.class, "META-INF/spring/applicationContext.xml");
            instance = l.getApplicationContext().getBean(Configuration.class);
        }
        return instance;
    }

    public String getPddlPath() {
        return pddlPath;
    }

    public void setPddlPath(String pddlPath) {
        this.pddlPath = pddlPath;
    }

    public Configuration() {
    }


    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    
    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getPhotosPath() {
        return photosPath;
    }

    public void setPhotosPath(String photosPath) {
        this.photosPath = photosPath;
    }

    public String getMrReportsPath() {
        return mrReportsPath;
    }

    public void setMrReportsPath(String mrReportsPath) {
        this.mrReportsPath = mrReportsPath;
    }

    public String getCsvsPath() {
        return cvssPath;
    }

    public void setCvssPath(String cvssPath) {
        this.cvssPath = cvssPath;
    }

    public String getDbPath() {
        return dbPath;
    }

    public void setDbPath(String dbPath) {
        this.dbPath = dbPath;
    }
    
    
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        String res = "-----------------------------------------------------" + nl;
        res += "Configuration: " + nl;
        res += "-----------------------------------------------------" + nl;
        res += "PROXY CONFIGURATION: " + nl;
        res += "proxy: " + this.proxy + " port: " + this.proxyPort + nl;
        res += "user: " + this.proxyUser + " password:" + this.proxyPassword + nl;
        res += "-----------------------------------------------------" + nl;
        res += "DB CONFIGURATION: " + nl;
        res += "user: " + this.dbUser + " password:" + this.dbPassword + nl;
        res += "-----------------------------------------------------" + nl;
        res += "PATIENTS' PHOTOS PATH: " + nl;
        res += "PATH: " + this.photosPath + nl;
        res += "-----------------------------------------------------" + nl;
        res += "PATIENTS' MR REPORTS PATH: " + nl;
        res += "PATH: " + this.mrReportsPath + nl;
        res += "-----------------------------------------------------" + nl;
        res += "CVSS PATH: " + nl;
        res += "PATH: " + this.cvssPath + nl;
        res += "-----------------------------------------------------" + nl;
        res += "DB PATH: " + nl;
        res += "PATH: " + this.dbPath + nl;
        res += "-----------------------------------------------------" + nl;
        
        return res;
    }

    public boolean isRlEnabled()
    {
        return JPAController.getInstance()
                .getEntityController(Exercise.class)
                .getAllEntities(ex -> ex.isEnabled() && ExerciseHelper.create(ex).isRLDriven())
                .stream()
                .findAny()
                .isPresent();
    }
}

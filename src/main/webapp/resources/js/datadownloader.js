function DataDownloader()
{

    this.downloadData = function(path, filename, delimiter=",")
    {
        $.get(
            path,
            {},
            function(data){
                let csv = createCSV(data["fields"], data["data"], delimiter)
                download(filename, csv, "text/csv;charset=utf-8;header=present")
            },
            "json"
        );
    }

    let downloader = this;

    this.downloadUsers = function()
    {
        downloader.downloadData("data/users", "users.csv");
    }

    this.downloadHistories = function()
    {
        downloader.downloadData("data/histories", "histories.csv");
    }

    this.downloadSessions = function()
    {
        downloader.downloadData("data/sessions", "sessions.csv", "\t");
    }

    this.downloadPersonalData = function()
    {
        downloader.downloadData("data/personaldata", "personaldata.csv");
    }

    this.downloadGroups = function()
    {
        downloader.downloadData("data/groups", "groups.csv");
    }

    this.downloadCenters = function()
    {
        downloader.downloadData("data/centers", "centers.csv");
    }
}
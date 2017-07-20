function getRootPath() {
    //获取当前路径
    var curPath = window.document.location.href;
    //获取相对路径
    var pathName = window.document.location.pathname;
    //获取域名地址
    var rootPath = curPath.substring(0, curPath.indexOf(pathName));
    return rootPath;
}

function getProjectPath() {
    //获取当前路径
    var curPath = window.document.location.href;
    //获取相对路径
    var pathName = window.document.location.pathname;
    //获取域名地址
    var rootPath = curPath.substring(0, curPath.indexOf(pathName));
    //获取项目名
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    return (rootPath + projectName + "/");
}
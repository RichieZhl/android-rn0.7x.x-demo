const fs = require('fs');
const path = require('path');
const crypto = require('crypto')

const commonIdModuleMapFile = 'common.map.json'

const cachePath = __dirname + path.sep + commonIdModuleMapFile
const projectNamePath = __dirname + path.sep + 'package.json'

function md5(content) {
  let sf = crypto.createHash('md5')
  // 对字符串进行加密
  sf.update(content)
  // 加密的二进制数据以字符串形式返回
  return sf.digest('hex')
}

let commonPath2IdModuleMap = null

const idMap = {}

let projectName = null

function createCommonModuleIdFactory() {
  // 已经存在的关系映射
  if (commonPath2IdModuleMap == null && fs.existsSync(cachePath)) {
    commonPath2IdModuleMap = JSON.parse(fs.readFileSync(cachePath))
  }
  if (projectName == null && fs.existsSync(projectNamePath)) {
    projectName = JSON.parse(fs.readFileSync(projectNamePath))['name']
  }

  return (modulePath) => {
    let filePath = modulePath.replace(__dirname, "")
    
    let id = commonPath2IdModuleMap[filePath]
    
    if (typeof id !== "number") {
      // 未在公共模块则返回uuid
      if (idMap[filePath]) {
        return idMap[filePath]
      }
      id = md5(projectName + filePath)
      idMap[filePath] = id
    }
    
    return id
  };
}

function processModuleFilter(module) {
  let modulePath = module['path'];

  let filePath = modulePath.replace(__dirname, "");

  if (filePath.indexOf("__prelude__") >= 0 ||
      filePath.indexOf('node_modules/metro-runtime/src/polyfills/') >= 0 ||
      filePath.indexOf('@react-native/polyfills') >= 0 ||
      filePath.indexOf("/node_modules/react-native/Libraries/polyfills") >= 0 ||
      filePath.indexOf("source-map") >= 0 ||
      filePath.indexOf("/node_modules/metro/src/lib/polyfills/") >= 0) {
    return false;
  }
  
  let id = commonPath2IdModuleMap[filePath];

  if (typeof id === 'number') {
    return false;
  }
  return true;
}

module.exports = {
  serializer: {
    createModuleIdFactory: createCommonModuleIdFactory,
    processModuleFilter: processModuleFilter,
  }
};

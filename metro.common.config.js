const fs = require('fs')
const path = require('path')

const commonIdModuleMapFile = 'common.map.json'

function createCommonModuleIdFactory() {
  let nextId = 1
  const cachePath = __dirname + path.sep + commonIdModuleMapFile

  // 已经存在的关系映射
  let commonId2ModuleMap = {}

  if (fs.existsSync(cachePath)) {
    commonId2ModuleMap = JSON.parse(fs.readFileSync(cachePath))

    if (!commonId2ModuleMap) {
      commonId2ModuleMap = {}
    }

    const ids = Object.values(commonId2ModuleMap)
  
    if (ids.length > 0) {
      nextId = Math.max(...ids) || 0
    }
  }

  return (modulePath) => {
    let filePath = modulePath.replace(__dirname, "");

    let id = commonId2ModuleMap[filePath];
    
    if (typeof id !== "number") {
      nextId++;
      
      id = nextId;
      
      if (!commonId2ModuleMap[filePath]) {
        commonId2ModuleMap[filePath] = id

        fs.writeFileSync(cachePath, JSON.stringify(commonId2ModuleMap))
      }
    }
    return id
  };
}

module.exports = {
  serializer: {
    createModuleIdFactory: createCommonModuleIdFactory,
  },
  transformer: {
    getTransformOptions: async () => ({
      transform: {
        experimentalImportSupport: false,
        inlineRequires: true,
      },
    }),
  },
}

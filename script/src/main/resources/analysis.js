function parse(api, code) {
  let program = acorn.parse(code);
  let declareInfo = getDeclareInfo(api, program);
  let invokeInfo = getMethodInvoke(program);
  return JSON.stringify({
    ...declareInfo,
    ...invokeInfo
  })
}

function getDeclareInfo(api, node) {
  if (node.body.length !== 1) {
    console.log(node.body.length)
    throw new Error("脚本中只能只能声明一个顶层语句");
  }
  let fun = node.body[0];
  if (fun.type !== 'FunctionDeclaration') {
    throw new Error("脚本中顶层语句必须是一个函数");
  }
  if (fun.id.name !== api) {
    throw new Error("顶层函数名称必须与api相同");
  }
  return {
    name: fun.id.name,
    params: fun.params.map(it => {
      return it.name
    })
  }
}

function getMethodInvoke(body) {
  let result = {
    globalInvoke: [],
    memberInvoke: [],
  };
  acorn.walk.full(body, (node, state, type) => {
    if (type === 'CallExpression') {
      let receiver = node.callee
      if (receiver.type === 'MemberExpression') {
        if (receiver.object.type === 'CallExpression') {
          //链式调用
        } else if (receiver.object.type === 'Identifier') {
          //变量上的调用
          result.memberInvoke.push({
            obj: receiver.object.name,
            method: receiver.property.name,
            arguments: node.arguments.map((item) => {
              return item.value
            }),
          })
        }
      } else if (receiver.type === 'Identifier') {
        //调用全局函数
        result.globalInvoke.push({
          method: receiver.name,
          arguments: node.arguments.map((item) => {
            return item.value
          }),
        })
      }
    }
  })
  return result
}

function getFirstFunctionInfo(node) {
  let fun = acorn.walk.findNodeAfter(node, node.start, (type, node) => {
    return node.type === 'CallExpression'
  });
  if (typeof (fun) == "undefined") {
    return null;
  }
  let funNode = fun.node;
  return {
    object: funNode.callee.object.name,
    method: funNode.callee.property.name,
    arguments: funNode.arguments.map((item) => {
      return item.value
    }),
  }
}


function parse(code) {
  let declareInfo = getDeclareInfo(code);
  let invokeInfo = getMethodInvoke(code);
  return JSON.stringify({
    ...declareInfo,
    ...invokeInfo
  })
}

function getDeclareInfo(code) {
  let node = acorn.parse(code);
  if (node.body.length !== 1) {
    console.log(node.body.length)
    throw new SyntaxError("脚本中只能只能声明一个顶层语句");
  }
  let fun = node.body[0];
  if (fun.type !== 'FunctionDeclaration') {
    throw new SyntaxError("脚本中顶层语句必须是一个函数");
  }
  return {
    name: fun.id.name,
    params: fun.params.map(it => {
      return it.name
    })
  }
}

function getMethodInvoke(code) {
  let body = acorn.parse(code);
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
            line: getLineNumber(code, node),
            params: getParameters(node),
          })
        }
      } else if (receiver.type === 'Identifier') {
        //调用全局函数
        result.globalInvoke.push({
          method: receiver.name,
          line: getLineNumber(code, node),
          params: getParameters(node),
        })
      }
    }
  })
  return result
}

/**
 * 获取节点行号
 * @param str {String}
 * @param node
 */
function getLineNumber(str, node) {
  return str.substr(0, node.start).match(/\n/g).length
}

function getParameters(node) {
  return node.arguments.map((item) => {
    if (item.type === 'Literal') {
      return {
        type: 'Literal',
        raw: item.raw
      }
    } else if (item.type === 'Identifier') {
      return {
        type: 'Identifier',
        raw: item.name
      }
    } else {
      return {
        type: 'Unknown'
      }
    }
  })
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


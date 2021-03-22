function analysis(type, api, newCode, oldCode) {
  let program = acorn.parse(newCode);
  beforeWalk(api, program);
  commonWalk(program)
  if (type === 'trigger') {
    triggerWalk(program)
  } else if (type === 'api') {
    apiWalk(program)
  } else if (type === 'function') {
    functionWalk(program, oldCode)
  } else {
    throw new Error("错误的代码类型");
  }
}

function beforeWalk(api, node) {
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

}

function commonWalk(node) {
  acorn.walk.full(node, (item, type) => {

  });
}

function triggerWalk(node) {

  acorn.walk.full(node, (item, type) => {

  });
}

function apiWalk(node) {

  acorn.walk.full(node, (item, type) => {

  });
}

function functionWalk(node, oldCode) {
  acorn.walk.full(acorn.parse(oldCode))

  acorn.walk.full(node, (item, type) => {

  });
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


import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.Modifier
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.expr.*
import com.github.javaparser.ast.stmt.BlockStmt
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt
import com.github.javaparser.ast.type.ClassOrInterfaceType
import io.entframework.med.model.Field

def cu = new CompilationUnit();

cu.setPackageDeclaration(object.package.value);

ClassOrInterfaceDeclaration clazz = cu.addClass(object.name.text + "DynamicSqlSupport");
clazz.setFinal(true)


clazz.addFieldWithInitializer(object.name.text, object.name.propertyName, new ObjectCreationExpr()
        .setType(new ClassOrInterfaceType(object.name.text)))
        .setPublic(true)
        .setStatic(true)
        .setFinal(true)

cu.addImport("org.mybatis.dynamic.sql.SqlColumn")

cu.addImport("org.mybatis.dynamic.sql.BasicColumn")
MethodCallExpr selectList = new MethodCallExpr("columnList");
selectList.setScope(new NameExpr("BasicColumn"))

for (Field field : object.getFields()) {
    Expression nameExpr = new NameExpr(object.name.propertyName)
    clazz.addFieldWithInitializer("SqlColumn<" + field.getJavaType().toString() + ">",
            field.name, new FieldAccessExpr(nameExpr, field.name), Modifier.Keyword.PUBLIC, Modifier.Keyword.STATIC, Modifier.Keyword.FINAL)

    selectList.addArgument(new NameExpr(field.name))
}

clazz.addFieldWithInitializer("BasicColumn[]", "selectList", selectList, Modifier.Keyword.PUBLIC, Modifier.Keyword.STATIC, Modifier.Keyword.FINAL)

cu.addImport("org.mybatis.dynamic.sql.AliasableSqlTable")

ClassOrInterfaceDeclaration innerClass = new ClassOrInterfaceDeclaration();
innerClass.setName(object.name.text)
innerClass.setPublic(true).setStatic(true).setFinal(true)
innerClass.addExtendedType("AliasableSqlTable<" + object.name.text + ">");


for (Field field : object.getFields()) {
    LiteralExpr nameExpr = new StringLiteralExpr(field.getColumn())
    innerClass.addFieldWithInitializer("SqlColumn<" + field.getJavaType().toString() + ">",
            field.name, new MethodCallExpr("column", nameExpr), Modifier.Keyword.PUBLIC, Modifier.Keyword.FINAL)
}


innerClass.addConstructor(Modifier.Keyword.PUBLIC)
        .setBody(new BlockStmt()
                .addStatement(new ExplicitConstructorInvocationStmt()
                .setThis(false)
                .addArgument(new StringLiteralExpr(object.table))
                .addArgument(new MethodReferenceExpr()
                        .setScope(new TypeExpr(new ClassOrInterfaceType(object.name.text))).setIdentifier("new"))))


clazz.addMember(innerClass)

runtime.setContent(cu.toString())

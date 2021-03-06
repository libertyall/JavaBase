package com.github.kuangcp.instantiation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 3/9/19-5:48 PM
 *
 * 实例化和构造器的关系
 *
 * clone 以及 serialize 创建对象时 不会调用构造器
 */
@Slf4j
public class InstantiationAndConstructorTest {

  @Test
  public void testInitByNew() {
    InstantiationAndConstructor instance = new InstantiationAndConstructor("name");
    log.info("instance={}", instance);
  }

  // 反射实例化对象方式 实际上调用的空构造器
  @Test
  public void testInitByReflect1() throws IllegalAccessException, InstantiationException {
    InstantiationAndConstructor.class.newInstance();
  }

  @Test
  public void testInitByReflect() throws ReflectiveOperationException {
    Constructor<InstantiationAndConstructor> constructor =
        InstantiationAndConstructor.class.getConstructor(String.class);

    String name = "get constructor by reflect";
    InstantiationAndConstructor domain = constructor.newInstance(name);
    assertThat(domain.getName(), equalTo(name));

    Method method = InstantiationAndConstructor.class.getMethod("setName", String.class);
    name = "by method";
    method.invoke(domain, name);
    assertThat(domain.getName(), equalTo(name));
  }

  @Test
  public void testInitByClone() throws CloneNotSupportedException {
    String name = "clone";
    InstantiationAndConstructor target = new InstantiationAndConstructor(name);

    log.info("start clone");
    InstantiationAndConstructor clone = target.clone();
    assertThat(target, equalTo(clone));

    assertThat(clone == target, equalTo(false));
    assertThat(clone.getName(), equalTo("clone"));
  }

  @Test
  public void testInitByDeserialize() throws IOException, ClassNotFoundException {
    InstantiationAndConstructor origin = new InstantiationAndConstructor("name");

    // 序列化
    log.info("start serialize");
    ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
    ObjectOutputStream output = new ObjectOutputStream(byteOutput);
    output.writeObject(origin);

    // 反序列化
    log.info("start deserialize");
    ByteArrayInputStream byteInput = new ByteArrayInputStream(byteOutput.toByteArray());
    ObjectInputStream input = new ObjectInputStream(byteInput);

    InstantiationAndConstructor deserialize = (InstantiationAndConstructor) input.readObject();
    assertThat(deserialize.getName(), equalTo("name"));

  }
}

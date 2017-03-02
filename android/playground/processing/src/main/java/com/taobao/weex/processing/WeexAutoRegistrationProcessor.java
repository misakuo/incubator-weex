package com.taobao.weex.processing;

import com.google.auto.service.AutoService;
import com.taobao.weex.annotations.WeexComponent;
import com.taobao.weex.annotations.WeexDomObject;
import com.taobao.weex.annotations.WeexModule;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 * Created by moxun on 17/3/1.
 */

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.taobao.weex.annotations.WeexModule",
        "com.taobao.weex.annotations.WeexComponent",
        "com.taobao.weex.annotations.WeexDomObject"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class WeexAutoRegistrationProcessor extends AbstractProcessor {

    private ProcessingEnvironment processingEnvironment;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.processingEnvironment = processingEnv;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        log("start annotation processing");
        processModule(roundEnv.getElementsAnnotatedWith(WeexModule.class));
        processComponent(roundEnv.getElementsAnnotatedWith(WeexComponent.class));
        processDomObject(roundEnv.getElementsAnnotatedWith(WeexDomObject.class));
        return true;
    }

    private void processModule(Set<? extends Element> moduleElements) {
        for (Element element : moduleElements) {
            if (element instanceof TypeElement) {
                WeexModule annotation = element.getAnnotation(WeexModule.class);
                String pkgName = ((TypeElement) element).getQualifiedName().toString();
                log("process " + pkgName);
                try {
                    FileObject desc = processingEnvironment.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "assets/weex_plugin/module/" + pkgName + ".properties");
                    OutputStream outputStream = desc.openOutputStream();

                    Properties properties = new Properties();
                    properties.setProperty("class", pkgName);
                    properties.setProperty("name", annotation.name());
                    properties.setProperty("canOverrideExistingModule", String.valueOf(annotation.canOverrideExistingModule()));
                    properties.setProperty("globalRegistration", String.valueOf(annotation.globalRegistration()));
                    properties.store(outputStream, "Properties for weex module " + element.getSimpleName().toString());

                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void processComponent(Set<? extends Element> componentElements) {
        for (Element element : componentElements) {
            if (element instanceof TypeElement) {
                WeexComponent annotation = element.getAnnotation(WeexComponent.class);
                String pkgName = ((TypeElement) element).getQualifiedName().toString();
                log("process " + pkgName);
                try {
                    FileObject desc = processingEnvironment.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "assets/weex_plugin/component/" + pkgName + ".properties");
                    OutputStream outputStream = desc.openOutputStream();

                    String names = "";
                    if (annotation.names() != null && annotation.names().length > 0) {
                        for (String name : annotation.names()) {
                            if ("".equals(names)) {
                                names = name;
                            } else {
                                names = names + "," + name;
                            }
                        }
                    }

                    Properties properties = new Properties();
                    properties.setProperty("class", pkgName);
                    properties.setProperty("names", names);
                    properties.setProperty("appendTree", String.valueOf(annotation.appendTree()));
                    properties.setProperty("usingHolder", String.valueOf(annotation.usingHolder()));
                    properties.setProperty("creator", annotation.creator());
                    properties.store(outputStream, "Properties for weex component " + element.getSimpleName().toString());

                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void processDomObject(Set<? extends Element> domObjectElements) {
        for (Element element : domObjectElements) {
            if (element instanceof TypeElement) {
                WeexDomObject annotation = element.getAnnotation(WeexDomObject.class);
                String pkgName = ((TypeElement) element).getQualifiedName().toString();
                log("process " + pkgName);
                try {
                    FileObject desc = processingEnvironment.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "assets/weex_plugin/dom/" + pkgName + ".properties");
                    OutputStream outputStream = desc.openOutputStream();

                    Properties properties = new Properties();
                    properties.setProperty("class", pkgName);
                    properties.setProperty("type", annotation.type());
                    properties.store(outputStream, "Properties for weex dom object " + element.getSimpleName().toString());

                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void log(String msg) {
        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
    }

    private void warn(String msg) {
        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.WARNING, msg);
    }

    private void err(String msg) {
        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
    }
}

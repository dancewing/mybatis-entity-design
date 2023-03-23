/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med;

import com.intellij.openapi.diagnostic.Logger;

public class MedJaxbService {

    private static final Logger logger = Logger.getInstance(MedJaxbService.class);
//
//    private static final SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//
//    private static MedJaxbService INSTANCE = new MedJaxbService();
//
//    private JAXBContext context;
//
//    private Schema schema;
//
//    public static MedJaxbService getInstance() {
//        return INSTANCE;
//    }
//
//    public JaxbMed toObject(InputStream stream) throws Exception {
//        context = getContext();
//        Unmarshaller unmarshaller = context.createUnmarshaller();
//        unmarshaller.setSchema(getSchema());
//        unmarshaller.setListener(new Unmarshaller.Listener() {
//            @Override
//            public void beforeUnmarshal(Object target, Object parent) {
//            }
//        });
//        JAXBElement<JaxbMed> med = unmarshaller.unmarshal(new StreamSource(new InputStreamReader(stream)),
//                JaxbMed.class);
//        if (med != null) {
//            return med.getValue();
//        }
//        return null;
//    }
//
//    public String toString(JaxbMed jaxbMed) throws Exception {
//        context = getContext();
//        Marshaller marshaller = context.createMarshaller();
//        marshaller.setSchema(getSchema());
//        StringWriter stringWriter = new StringWriter();
//        marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,
//                "http://entframework.io/ns/mybatis https://mybatis-generator.oss-cn-shanghai.aliyuncs.com/mybatis-mapping-bindings-1.0.xsd");
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//        marshaller.marshal(jaxbMed, stringWriter);
//        return stringWriter.toString();
//    }
//
//    private Schema getSchema() throws Exception {
//        if (schema == null) {
//            schema = sf.newSchema(
//                    new StreamSource(MedJaxbService.class.getResourceAsStream("/mybatis-mapping-bindings-1.0.xsd")));
//        }
//        return schema;
//    }
//
//    private JAXBContext getContext() throws Exception {
//        if (context == null) {
//            context = JAXBContext.newInstance(JaxbMed.class);
//        }
//        return context;
//    }

}

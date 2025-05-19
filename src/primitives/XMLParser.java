package primitives;

import geometries.Geometry;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import renderer.Camera;
import renderer.RayTracerType;
import scene.Scene;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {

    public static String folderLocation = "XML Documents For Image Generation/";

    public static boolean elementExists(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        return nodeList != null && nodeList.getLength() > 0;
    }

    public static Double3 parseDouble3(Element parent) {
        var res = new Double3(
                Double.parseDouble(parent.getElementsByTagName("x").item(0).getTextContent()),
                Double.parseDouble(parent.getElementsByTagName("y").item(0).getTextContent()),
                Double.parseDouble(parent.getElementsByTagName("z").item(0).getTextContent())
        );
        return res;
    }

    public static Color parseToColor(Element parent) {
        return new Color(
                Double.parseDouble(parent.getElementsByTagName("r").item(0).getTextContent()),
                Double.parseDouble(parent.getElementsByTagName("g").item(0).getTextContent()),
                Double.parseDouble(parent.getElementsByTagName("b").item(0).getTextContent())
        );
    }

    public static List<Point> parseToPoints(Element parent) {
        NodeList pointsNodeList = parent.getChildNodes();
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < pointsNodeList.getLength(); i++) {
            Node node = pointsNodeList.item(i);
            // Skip non-element nodes (like text nodes, comments, etc.)
            if (node.getNodeType() != Node.ELEMENT_NODE) continue;
            Element pointElement = (Element) pointsNodeList.item(i);
            points.add(new Point(parseDouble3(pointElement)));
        }
        return points;
    }

    public static List<Double> parseToCoefficients(Element parent) {
        return List.of(
                Double.parseDouble(parent.getElementsByTagName("Constant").item(0).getTextContent()),
                Double.parseDouble(parent.getElementsByTagName("Linear").item(0).getTextContent()),
                Double.parseDouble(parent.getElementsByTagName("Quadratic").item(0).getTextContent())
        );
    }

    public static Material parseToMaterial(Element parent) {
        Material material = new Material();
        if (elementExists(parent, "Ambient"))
            material.setKA(parseDouble3((Element) parent.getElementsByTagName("Ambient").item(0)));
        if (elementExists(parent, "Diffuse"))
            material.setKD(parseDouble3((Element) parent.getElementsByTagName("Diffuse").item(0)));
        if (elementExists(parent, "Specular"))
            material.setKS(parseDouble3((Element) parent.getElementsByTagName("Specular").item(0)));
        if (elementExists(parent, "Shininess"))
            material.setShininess(Integer.parseInt(parent.getElementsByTagName("Shininess").item(0).getTextContent()));
        return material;
    }

    public static Scene SceneBuilder(Element scene) {
        Scene sceneRes = new Scene((scene.getElementsByTagName("Name").item(0)).getTextContent());

        // Parse the background color
        if (elementExists(scene, "BackgroundColor"))
            sceneRes.setBackground(parseToColor((Element) scene.getElementsByTagName("BackgroundColor").item(0)));

        // Setting up ambient light
        sceneRes.setAmbientLight(new AmbientLight(parseToColor((Element) scene.getElementsByTagName("AmbientLight").item(0))));


        // Setting up geometries
        NodeList geometries = scene.getElementsByTagName("Geometries").item(0).getChildNodes();
        for (int i = 0; i < geometries.getLength(); i++) {
            Node geometryNode = geometries.item(i);

            // Skip non-element nodes
            if (geometryNode.getNodeType() != Node.ELEMENT_NODE) continue;

            // Common parsing for all geometries
            Element geometryElement = (Element) geometryNode;
            Element points = (Element) geometryElement.getElementsByTagName("Points").item(0);

            Material material = parseToMaterial(geometryElement);
            Color emission = parseToColor((Element) geometryElement.getElementsByTagName("Emission").item(0));
            Geometry geometry = null;

            switch (geometryElement.getNodeName()) {
                case "Sphere": {
                    Point center = new Point(parseDouble3((Element) geometryElement.getElementsByTagName("Center").item(0)));
                    double radius = Double.parseDouble(geometryElement.getElementsByTagName("Radius").item(0).getTextContent());
                    geometry = new Sphere(center, radius);
                    break;
                }
                case "Triangle": {
                    List<Point> pointsRes = parseToPoints(points);
                    geometry = new Triangle(pointsRes.get(0), pointsRes.get(1), pointsRes.get(2));
                    break;
                }
                case "Plane": {
                    if (elementExists(geometryElement, "Normal")) {
                        Vector normal = new Vector(parseDouble3((Element) geometryElement.getElementsByTagName("Normal").item(0)));
                        Point p1 = new Point(parseDouble3((Element) points.getElementsByTagName("Point1").item(0)));
                        geometry = new Plane(normal, p1);
                    } else {
                        List<Point> pointsRes = parseToPoints(points);
                        geometry = new Plane(pointsRes.get(0), pointsRes.get(1), pointsRes.get(2));
                    }
                    break;
                }
                // Add more geometries as needed
                default:
                    throw new IllegalArgumentException("Unknown geometry type: " + geometryElement.getNodeName());
            }
            sceneRes.geometries.add(geometry.setMaterial(material).setEmission(emission));
        }

        // Check if any external lights are defined
        if (!elementExists(scene, "ExternalLights")) return sceneRes;


        // Setting up external lights
        NodeList lights = scene.getElementsByTagName("ExternalLights").item(0).getChildNodes();
        for (int i = 0; i < lights.getLength(); i++) {
            Node node = lights.item(i);
            // Skip non-element nodes (like text nodes, comments, etc.)
            if (node.getNodeType() != Node.ELEMENT_NODE) continue;

            Element lightElement = (Element) node;
            Color intensity = parseToColor(lightElement);

            switch (lightElement.getNodeName()) {
                case "DirectionalLight": {
                    Vector direction = new Vector(parseDouble3((Element) lightElement.getElementsByTagName("Direction").item(0)));
                    sceneRes.lights.add(new DirectionalLight(intensity, direction));
                    break;
                }
                case "PointLight": {
                    Point point = new Point(parseDouble3((Element) lightElement.getElementsByTagName("Position").item(0)));
                    List<Double> listOfCoefficients = parseToCoefficients((Element) lightElement.getElementsByTagName("Attenuation").item(0));
                    sceneRes.lights.add(new PointLight(intensity, point)
                            .setKc(listOfCoefficients.get(0))
                            .setKl(listOfCoefficients.get(1))
                            .setKq(listOfCoefficients.get(2)));
                    break;
                }
                case "SpotLight": {
                    Point point = new Point(parseDouble3((Element) lightElement.getElementsByTagName("Position").item(0)));
                    Vector direction = new Vector(parseDouble3((Element) lightElement.getElementsByTagName("Direction").item(0)));
                    List<Double> listOfCoefficients = parseToCoefficients((Element) lightElement.getElementsByTagName("Attenuation").item(0));
                    double narrowBeam = Double.parseDouble(lightElement.getElementsByTagName("NarrowBeam").item(0).getTextContent());
                    sceneRes.lights.add(new SpotLight(intensity, point, direction)
                            .setKc(listOfCoefficients.get(0))
                            .setKl(listOfCoefficients.get(1))
                            .setKq(listOfCoefficients.get(2))
                            .setNarrowBeam(narrowBeam));
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unknown light type: " + lightElement.getNodeName());
            }
        }

        return sceneRes;
    }

    public static Scene BuildSceneFromXML(String xmlFileName) {
        try {
            // Load and parse the XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFileName);
            document.getDocumentElement().normalize();

            Element root = (Element) document.getElementsByTagName("Scene").item(0);
            return SceneBuilder(root);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse the XML file and build the Scene object.");
        }
    }

    public static Camera BuildCameraFromXML(String xmlFileName) {
        try {
            // Load and parse the XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFileName);
            document.getDocumentElement().normalize();

            Camera.Builder cameraBuilder = new Camera.Builder();

            Element root = (Element) document.getElementsByTagName("Camera").item(0);

            // Parse camera position
            Element position = (Element) root.getElementsByTagName("Position").item(0);
            double x = Double.parseDouble(position.getElementsByTagName("x").item(0).getTextContent());
            double y = Double.parseDouble(position.getElementsByTagName("y").item(0).getTextContent());
            double z = Double.parseDouble(position.getElementsByTagName("z").item(0).getTextContent());
            cameraBuilder.setLocation(new Point((int) x, (int) y, (int) z));

            // Parse camera direction
            Element directions = (Element) root.getElementsByTagName("Directions").item(0);
            if (elementExists(directions, "Direction1")) {
                Element directionTwoVectors = (Element) directions.getElementsByTagName("Direction1").item(0);
                Vector vToRes = new Vector(parseDouble3((Element) directionTwoVectors.getElementsByTagName("vTo").item(0)));
                Vector vUpRes = new Vector(parseDouble3((Element) directionTwoVectors.getElementsByTagName("vUp").item(0)));
                cameraBuilder.setDirection(vToRes, vUpRes);
            }

            // Additional camera directions
            if (elementExists(directions, "Direction2")) {
                Element directionTwoVectors = (Element) directions.getElementsByTagName("Direction2").item(0);

                Point pointRes = new Point(
                        parseDouble3((Element) directionTwoVectors.getElementsByTagName("Point").item(0))
                );
                Vector vUpRes2 = new Vector(
                        parseDouble3((Element) directionTwoVectors.getElementsByTagName("vUp").item(0))
                );
                cameraBuilder.setDirection(pointRes, vUpRes2);
            }

            if (elementExists(directions, "Direction3")) {
                Element directionTwoVectors = (Element) directions.getElementsByTagName("Direction3").item(0);

                Point pointRes = new Point(
                        parseDouble3((Element) directionTwoVectors.getElementsByTagName("LookAtPoint").item(0))
                );
                cameraBuilder.setDirection(pointRes);
            }

            // Parse camera view plane size and distance
            Element size = (Element) root.getElementsByTagName("Size").item(0);
            double width = Double.parseDouble(size.getElementsByTagName("Width").item(0).getTextContent());
            double height = Double.parseDouble(size.getElementsByTagName("Height").item(0).getTextContent());
            cameraBuilder.setVpSize(width, height);

            Element distance = (Element) root.getElementsByTagName("Distance").item(0);
            double distanceValue = Double.parseDouble(distance.getTextContent());
            cameraBuilder.setVpDistance(distanceValue);

            // Parse camera resolution
            Element resolution = (Element) root.getElementsByTagName("Resolution").item(0);
            int xRes = Integer.parseInt(resolution.getElementsByTagName("nX").item(0).getTextContent());
            int yRes = Integer.parseInt(resolution.getElementsByTagName("nY").item(0).getTextContent());
            cameraBuilder.setResolution(xRes, yRes);

            // Parse camera ray tracer
            Element rayTracer = (Element) root.getElementsByTagName("RayTracer").item(0);
            RayTracerType typeRes = RayTracerType.valueOf(rayTracer.getElementsByTagName("RayTracerType").item(0).getTextContent());
            cameraBuilder.setRayTracer(SceneBuilder((Element) rayTracer.getElementsByTagName("Scene").item(0)), typeRes);

            return cameraBuilder.build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse the XML file and build the Camera object.");
        }
    }
}

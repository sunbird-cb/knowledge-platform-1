package org.sunbird.actors

import java.util

import akka.actor.Props
import org.scalamock.scalatest.MockFactory
import org.sunbird.common.dto.{Property, Request, Response}
import org.sunbird.graph.dac.model.{Node, Relation, SearchCriteria}
import org.sunbird.graph.nodes.DataNode.getRelationMap
import org.sunbird.graph.utils.ScalaJsonUtils
import org.sunbird.graph.{GraphService, OntologyEngineContext}
import org.sunbird.kafka.client.KafkaClient

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class QuestionSetActorTest extends BaseSpec with MockFactory {

    "questionSetActor" should "return failed response for 'unknown' operation" in {
        implicit val oec: OntologyEngineContext = new OntologyEngineContext
        testUnknownOperation(Props(new QuestionSetActor()), getQuestionSetRequest())
    }

    it should "return success response for 'createQuestionSet'" in {
        implicit val oec: OntologyEngineContext = mock[OntologyEngineContext]
        val graphDB = mock[GraphService]
        (oec.graphService _).expects().returns(graphDB).anyNumberOfTimes()
        val node = getNode("QuestionSet", None)
        (graphDB.addNode(_: String, _: Node)).expects(*, *).returns(Future(node))
        (graphDB.readExternalProps(_: Request, _: List[String])).expects(*, *).returns(Future(new Response())).anyNumberOfTimes()
        val request = getQuestionSetRequest()
        request.getContext.put("identifier", "do1234")
        request.putAll(mapAsJavaMap(Map("name" -> "question_1",
            "visibility" -> "Default",
            "code" -> "finemanfine",
            "navigationMode" -> "linear",
            "allowSkip" -> "Yes",
            "requiresSubmit" -> "No",
            "shuffle" -> "Yes",
            "showFeedback" -> "Yes",
            "showSolutions" -> "Yes",
            "showHints" -> "Yes",
            "summaryType" -> "Complete",
            "mimeType" -> "application/vnd.ekstep.questionset",
            "primaryCategory" -> "Practice Question Set")))
        request.setOperation("createQuestionSet")
        val response = callActor(request, Props(new QuestionSetActor()))
        assert("successful".equals(response.getParams.getStatus))
    }

    it should "return success response for 'readQuestionSet'" in {
        implicit val oec: OntologyEngineContext = mock[OntologyEngineContext]
        val graphDB = mock[GraphService]
        (oec.graphService _).expects().returns(graphDB).anyNumberOfTimes()
        val node = getNode("QuestionSet", Some(new util.HashMap[String, AnyRef]() {
            {
                put("name", "QuestionSet")
                put("description", "Updated question Set")
            }
        }))
        (graphDB.getNodeByUniqueId(_: String, _: String, _: Boolean, _: Request)).expects(*, *, *, *).returns(Future(node))
        val request = getQuestionSetRequest()
        request.getContext.put("identifier", "do1234")
        request.putAll(mapAsJavaMap(Map("identifier" -> "do_1234", "fields" -> "")))
        request.setOperation("readQuestionSet")
        val response = callActor(request, Props(new QuestionSetActor()))
        assert("successful".equals(response.getParams.getStatus))
    }

    it should "return success response for 'updateQuestionSet'" in {
        implicit val oec: OntologyEngineContext = mock[OntologyEngineContext]
        val graphDB = mock[GraphService]
        (oec.graphService _).expects().returns(graphDB).anyNumberOfTimes()
        val node = getNode("QuestionSet", None)
        node.getMetadata.putAll(mapAsJavaMap(Map("name" -> "question_1",
            "visibility" -> "Default",
            "code" -> "finemanfine",
            "description" -> "Updated description",
            "navigationMode" -> "linear",
            "allowSkip" -> "Yes",
            "requiresSubmit" -> "No",
            "shuffle" -> "Yes",
            "showFeedback" -> "Yes",
            "showSolutions" -> "Yes",
            "showHints" -> "Yes",
            "summaryType" -> "Complete",
            "mimeType" -> "application/vnd.ekstep.questionset",
            "primaryCategory" -> "Practice Question Set")))
        (graphDB.upsertNode(_: String, _: Node, _: Request)).expects(*, *, *).returns(Future(node))
        (graphDB.getNodeByUniqueId(_: String, _: String, _: Boolean, _: Request)).expects(*, *, *, *).returns(Future(node)).atLeastOnce()
        //		(graphDB.getNodeProperty(_: String, _: String, _: String)).expects(*, *, *).returns(Future(new Property("versionKey", new org.neo4j.driver.internal.value.StringValue("1234"))))
        val request = getQuestionSetRequest()
        request.getContext.put("identifier", "do1234")
        request.putAll(mapAsJavaMap(Map("versionKey" -> "1234", "description" -> "updated desc")))
        request.setOperation("updateQuestionSet")
        val response = callActor(request, Props(new QuestionSetActor()))
        assert("successful".equals(response.getParams.getStatus))
    }

    it should "return success response for 'reviewQuestionSet'" in {
        implicit val oec: OntologyEngineContext = mock[OntologyEngineContext]
        val graphDB = mock[GraphService]
        (oec.graphService _).expects().returns(graphDB).anyNumberOfTimes()
        val node = getNode("QuestionSet", None)
        node.getMetadata.putAll(mapAsJavaMap(Map("name" -> "question_1",
            "visibility" -> "Default",
            "code" -> "finemanfine",
            "navigationMode" -> "linear",
            "allowSkip" -> "Yes",
            "requiresSubmit" -> "No",
            "shuffle" -> "Yes",
            "showFeedback" -> "Yes",
            "showSolutions" -> "Yes",
            "showHints" -> "Yes",
            "summaryType" -> "Complete",
            "versionKey" -> "1234",
            "mimeType" -> "application/vnd.ekstep.questionset",
            "primaryCategory" -> "Practice Question Set")))
        (graphDB.upsertNode(_: String, _: Node, _: Request)).expects(*, *, *).returns(Future(node))
        (graphDB.getNodeByUniqueId(_: String, _: String, _: Boolean, _: Request)).expects(*, *, *, *).returns(Future(node)).atLeastOnce()
        //		(graphDB.getNodeProperty(_: String, _: String, _: String)).expects(*, *, *).returns(Future(new Property("versionKey", new org.neo4j.driver.internal.value.StringValue("1234"))))
        val request = getQuestionSetRequest()
        request.getContext.put("identifier", "do1234")
        request.putAll(mapAsJavaMap(Map("versionKey" -> "1234", "description" -> "updated desc")))
        request.setOperation("reviewQuestionSet")
        val response = callActor(request, Props(new QuestionSetActor()))
        assert("successful".equals(response.getParams.getStatus))
    }

    it should "return success response for 'retireQuestionSet" +
        "'" in {
        implicit val oec: OntologyEngineContext = mock[OntologyEngineContext]
        val graphDB = mock[GraphService]
        (oec.graphService _).expects().returns(graphDB).anyNumberOfTimes()
        val node = getNode("QuestionSet", None)
        node.getMetadata.putAll(mapAsJavaMap(Map("name" -> "question_1",
            "visibility" -> "Default",
            "code" -> "finemanfine",
            "navigationMode" -> "linear",
            "allowSkip" -> "Yes",
            "requiresSubmit" -> "No",
            "shuffle" -> "Yes",
            "showFeedback" -> "Yes",
            "showSolutions" -> "Yes",
            "showHints" -> "Yes",
            "summaryType" -> "Complete",
            "mimeType" -> "application/vnd.ekstep.questionset",
            "primaryCategory" -> "Practice Question Set")))
        (graphDB.getNodeByUniqueId(_: String, _: String, _: Boolean, _: Request)).expects(*, *, *, *).returns(Future(node)).atLeastOnce()
        (graphDB.updateNodes(_: String, _: util.List[String], _: util.HashMap[String, AnyRef])).expects(*, *, *).returns(Future(new util.HashMap[String, Node]))
        val request = getQuestionSetRequest()
        request.getContext.put("identifier", "do1234")
        request.putAll(mapAsJavaMap(Map("versionKey" -> "1234", "description" -> "updated desc")))
        request.setOperation("retireQuestionSet")
        val response = callActor(request, Props(new QuestionSetActor()))
        assert("successful".equals(response.getParams.getStatus))
    }

    it should "return success response for 'publishQuestionSet" in {
        implicit val oec: OntologyEngineContext = mock[OntologyEngineContext]
        val graphDB = mock[GraphService]
        val kfClient = mock[KafkaClient]
        (oec.kafkaClient _).expects().returns(kfClient).anyNumberOfTimes()
        (oec.graphService _).expects().returns(graphDB).anyNumberOfTimes()
        val node = getNode("QuestionSet", None)
        node.getMetadata.putAll(mapAsJavaMap(Map("name" -> "question_1",
            "visibility" -> "Default",
            "code" -> "finemanfine",
            "navigationMode" -> "linear",
            "allowSkip" -> "Yes",
            "requiresSubmit" -> "No",
            "shuffle" -> "Yes",
            "showFeedback" -> "Yes",
            "showSolutions" -> "Yes",
            "showHints" -> "Yes",
            "summaryType" -> "Complete",
            "mimeType" -> "application/vnd.ekstep.questionset",
            "primaryCategory" -> "Practice Question Set")))
        (graphDB.getNodeByUniqueId(_: String, _: String, _: Boolean, _: Request)).expects(*, *, *, *).returns(Future(node)).atLeastOnce()
        (kfClient.send(_: String, _: String)).expects(*, *).once()
        val request = getQuestionSetRequest()
        request.getContext.put("identifier", "do1234")
        request.putAll(mapAsJavaMap(Map("versionKey" -> "1234", "description" -> "updated desc")))
        request.setOperation("publishQuestionSet")
        val response = callActor(request, Props(new QuestionSetActor()))
        assert("successful".equals(response.getParams.getStatus))
    }

    it should "return success response for 'addQuestion'" in {
        implicit val oec: OntologyEngineContext = mock[OntologyEngineContext]
        val graphDB = mock[GraphService]
        (oec.graphService _).expects().returns(graphDB).anyNumberOfTimes()
        val node = getNode("QuestionSet", None)
        node.setIdentifier("do_1234")
        node.getMetadata.putAll(mapAsJavaMap(Map("name" -> "question_1",
            "visibility" -> "Default",
            "code" -> "finemanfine",
            "navigationMode" -> "linear",
            "allowSkip" -> "Yes",
            "requiresSubmit" -> "No",
            "shuffle" -> "Yes",
            "showFeedback" -> "Yes",
            "showSolutions" -> "Yes",
            "showHints" -> "Yes",
            "summaryType" -> "Complete",
            "versionKey" -> "1234",
            "mimeType" -> "application/vnd.ekstep.questionset",
            "primaryCategory" -> "Practice Question Set")))
        (graphDB.upsertNode(_: String, _: Node, _: Request)).expects(*, *, *).returns(Future(node))
        (graphDB.getNodeByUniqueId(_: String, _: String, _: Boolean, _: Request)).expects(*, *, *, *).returns(Future(node)).anyNumberOfTimes()
        (graphDB.getNodeByUniqueIds(_: String, _: SearchCriteria)).expects(*, *).returns(Future(List(node).asJava)).anyNumberOfTimes()
        (graphDB.readExternalProps(_: Request, _: List[String])).expects(*, *).returns(Future(getCassandraHierarchy())).anyNumberOfTimes
        (graphDB.saveExternalProps(_: Request)).expects(*).returns(Future(new Response())).anyNumberOfTimes
        val request = getQuestionSetRequest()
        request.getContext.put("identifier", "do1234")
        request.putAll((Map("children" -> List("do_749").asJava.asInstanceOf[AnyRef], "rootId" -> "do1234")).asJava)
        request.setOperation("addQuestion")
        val response = callActor(request, Props(new QuestionSetActor()))
        assert("successful".equals(response.getParams.getStatus))
    }

    it should "return success response for 'removeQuestion'" in {
        implicit val oec: OntologyEngineContext = mock[OntologyEngineContext]
        val graphDB = mock[GraphService]
        (oec.graphService _).expects().returns(graphDB).anyNumberOfTimes()
        val node = getNode("QuestionSet", None)
        node.setIdentifier("do_1234")
        node.getMetadata.putAll(mapAsJavaMap(Map("name" -> "question_1",
            "visibility" -> "Default",
            "code" -> "finemanfine",
            "navigationMode" -> "linear",
            "allowSkip" -> "Yes",
            "requiresSubmit" -> "No",
            "shuffle" -> "Yes",
            "showFeedback" -> "Yes",
            "showSolutions" -> "Yes",
            "showHints" -> "Yes",
            "summaryType" -> "Complete",
            "versionKey" -> "1234",
            "mimeType" -> "application/vnd.ekstep.questionset",
            "primaryCategory" -> "Practice Question Set")))
        (graphDB.upsertNode(_: String, _: Node, _: Request)).expects(*, *, *).returns(Future(node))
        (graphDB.getNodeByUniqueId(_: String, _: String, _: Boolean, _: Request)).expects(*, *, *, *).returns(Future(node)).anyNumberOfTimes()
        (graphDB.getNodeByUniqueIds(_: String, _: SearchCriteria)).expects(*, *).returns(Future(List(node).asJava)).anyNumberOfTimes()
        (graphDB.readExternalProps(_: Request, _: List[String])).expects(*, *).returns(Future(getCassandraHierarchy())).anyNumberOfTimes
        (graphDB.saveExternalProps(_: Request)).expects(*).returns(Future(new Response())).anyNumberOfTimes
        val request = getQuestionSetRequest()
        request.getContext.put("identifier", "do1234")
        request.putAll((Map("children" -> List("do_914").asJava.asInstanceOf[AnyRef], "rootId" -> "do1234")).asJava)
        request.setOperation("removeQuestion")
        val response = callActor(request, Props(new QuestionSetActor()))
        assert("successful".equals(response.getParams.getStatus))
    }

    private def getQuestionSetRequest(): Request = {
        val request = new Request()
        request.setContext(new java.util.HashMap[String, AnyRef]() {
            {
                put("graph_id", "domain")
                put("version", "1.0")
                put("objectType", "QuestionSet")
                put("schemaName", "questionset")
            }
        })
        request.setObjectType("QuestionSet")
        request
    }


    private def getRelationNode(): Node = {
        val node = new Node()
        node.setGraphId("domain")
        node.setIdentifier("do_749")
        node.setMetadata(new util.HashMap[String, AnyRef]() {
            {
                put("identifier", "do_749")
                put("mimeType", "application/vnd.ekstep.qml-archive")
                put("visibility", "Default")
                put("status", "Draft")
                put("primaryCategory", "Practice Question Set")
            }
        })
        node.setObjectType("Question")
        node.setNodeType("DATA_NODE")
        node
    }

    private def getRelationNode_1(): Node = {
        val node = new Node()
        node.setGraphId("domain")
        node.setIdentifier("do_914")
        node.setMetadata(new util.HashMap[String, AnyRef]() {
            {
                put("identifier", "do_914")
                put("visibility", "Default")
                put("mimeType", "application/vnd.ekstep.qml-archive")
                put("status", "Draft")
                put("primaryCategory", "Practice Question Set")
            }
        })
        node.setObjectType("Question")
        node.setNodeType("DATA_NODE")
        node
    }

    def getDefinitionNode(): Node = {
        val node = new Node()
        node.setIdentifier("obj-cat:practice-question-set_question-set_all")
        node.setNodeType("DATA_NODE")
        node.setObjectType("ObjectCategoryDefinition")
        node.setGraphId("domain")
        node.setMetadata(mapAsJavaMap(
            ScalaJsonUtils.deserialize[Map[String, AnyRef]]("{\n    \"objectCategoryDefinition\": {\n      \"name\": \"Learning Resource\",\n      \"description\": \"Content Playlist\",\n      \"categoryId\": \"obj-cat:practice_question_set\",\n      \"targetObjectType\": \"Content\",\n      \"objectMetadata\": {\n        \"config\": {},\n        \"schema\": {\n          \"required\": [\n            \"author\",\n            \"copyright\",\n            \"license\",\n            \"audience\"\n          ],\n          \"properties\": {\n            \"audience\": {\n              \"type\": \"array\",\n              \"items\": {\n                \"type\": \"string\",\n                \"enum\": [\n                  \"Student\",\n                  \"Teacher\"\n                ]\n              },\n              \"default\": [\n                \"Student\"\n              ]\n            },\n            \"mimeType\": {\n              \"type\": \"string\",\n              \"enum\": [\n                \"application/pdf\"\n              ]\n            }\n          }\n        }\n      }\n    }\n  }")))
        node
    }

    def getCassandraHierarchy(): Response = {
        val hierarchyString: String = """{"children":[{"parent":"do_113165166851596288123","totalQuestions":0,"code":"QS_V_Parent_Old","allowSkip":"No","description":"QS-2_parent","language":["English"],"mimeType":"application/vnd.ekstep.questionset","showHints":"No","createdOn":"2020-12-04T15:31:45.948+0530","objectType":"QuestionSet","primaryCategory":"Practice Question Set","lastUpdatedOn":"2020-12-04T15:31:45.947+0530","showSolutions":"No","identifier":"do_11316516745992601613","lastStatusChangedOn":"2020-12-04T15:31:45.948+0530","requiresSubmit":"No","visibility":"Parent","maxQuestions":0,"index":1,"setType":"materialised","languageCode":["en"],"version":1,"versionKey":"1607076105948","showFeedback":"No","depth":1,"name":"QS_V_Parent_2","navigationMode":"non-linear","shuffle":"Yes","status":"Draft"},{"parent":"do_113165166851596288123","totalQuestions":0,"code":"QS_V_Parent_New","allowSkip":"No","description":"QS-1_parent","language":["English"],"mimeType":"application/vnd.ekstep.questionset","showHints":"No","createdOn":"2020-12-04T15:31:45.872+0530","objectType":"QuestionSet","primaryCategory":"Practice Question Set","children":[{"parent":"do_11316516745922969611","identifier":"do_11316399038283776016","lastStatusChangedOn":"2020-12-02T23:36:59.783+0530","code":"question.code","visibility":"Default","index":1,"language":["English"],"mimeType":"application/vnd.ekstep.qml-archive","languageCode":["en"],"createdOn":"2020-12-02T23:36:59.783+0530","version":1,"objectType":"Question","versionKey":"1606932419783","depth":2,"primaryCategory":"Practice Question Set","name":"question_1","lastUpdatedOn":"2020-12-02T23:36:59.783+0530","status":"Draft"}],"lastUpdatedOn":"2020-12-04T15:31:45.861+0530","showSolutions":"No","identifier":"do_11316516745922969611","lastStatusChangedOn":"2020-12-04T15:31:45.876+0530","requiresSubmit":"No","visibility":"Parent","maxQuestions":0,"index":2,"setType":"materialised","languageCode":["en"],"version":1,"versionKey":"1607076105872","showFeedback":"No","depth":1,"name":"QS_V_Parent_1","navigationMode":"non-linear","shuffle":"Yes","status":"Draft"},{"identifier":"do_11315445058114355211","parent":"do_113165166851596288123","lastStatusChangedOn":"2020-11-19T12:08:13.854+0530","code":"finemanfine","visibility":"Default","index":4,"language":["English"],"mimeType":"application/vnd.ekstep.qml-archive","languageCode":["en"],"createdOn":"2020-11-19T12:08:13.854+0530","version":1,"objectType":"Question","versionKey":"1605767893854","depth":1,"name":"question_1","lastUpdatedOn":"2020-11-19T12:08:13.854+0530","contentType":"Resource","status":"Draft"},{"identifier":"do_11315319237189632011","parent":"do_113165166851596288123","lastStatusChangedOn":"2020-11-17T17:28:23.277+0530","code":"finemanfine","visibility":"Default","index":3,"language":["English"],"mimeType":"application/vnd.ekstep.qml-archive","languageCode":["en"],"createdOn":"2020-11-17T17:28:23.277+0530","version":1,"objectType":"Question","versionKey":"1605614303277","depth":1,"name":"question_1","lastUpdatedOn":"2020-11-17T17:28:23.277+0530","contentType":"Resource","status":"Draft"}],"identifier":"do_113165166851596288123"}"""
        val response = new Response
        response.put("hierarchy", hierarchyString)
    }

}
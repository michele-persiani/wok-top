package it.unibo.msrehab.services;


import it.unibo.msrehab.model.EntityController;
import it.unibo.msrehab.model.JPAModel;
import it.unibo.msrehab.model.entities.*;
import it.unibo.msrehab.util.DataJsonCollector;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class DataJsonService
{
    private final EntityController<MSRUser> userController = JPAModel.getInstance().getEntityController(MSRUser.class);
    private final EntityController<History> historyController = JPAModel.getInstance().getEntityController(History.class);
    private final EntityController<MSRCenter> centerController = JPAModel.getInstance().getEntityController(MSRCenter.class);
    private final EntityController<MSRGroup> groupController = JPAModel.getInstance().getEntityController(MSRGroup.class);
    private final EntityController<PersonalData> personalDataController = JPAModel.getInstance().getEntityController(PersonalData.class);
    private final EntityController<MSRSession> sessionController = JPAModel.getInstance().getEntityController(MSRSession.class);


    @RequestMapping(value="/data/users", method = RequestMethod.GET)
    public ResponseEntity<String> getUsersJson()
    {
        String json = userController.getAllEntities()
                .stream()
                .collect(
                        new DataJsonCollector<MSRUser>()
                                .addColumn("id", BaseEntity::getId)
                                .addColumn("role", MSRUser::getMsrrole)
                                .addColumn("username", MSRUser::getUsername)
                                .addColumn("password", MSRUser::getPassword)
                                .addColumn("firstname", MSRUser::getName)
                                .addColumn("lastname", MSRUser::getSurname)
                                .addColumn("email", MSRUser::getEmail)
                                .addColumn("phone", MSRUser::getPhone)
                                .addColumn("center", MSRUser::getCid)
                                .addColumn("group", MSRUser::getGid)

                );
        return ResponseEntity.ok(json);
    }

    @RequestMapping(value="/data/histories", method = RequestMethod.GET)
    public ResponseEntity<String> getHistoryJson()
    {
        String json = historyController.getAllEntities()
                .stream()
                .collect(
                        new DataJsonCollector<History>()
                                .addColumn("id", History::getId)
                                .addColumn("userid", History::getUserid)
                                .addColumn("exerciseid", History::getExid)
                                .addColumn("sessionid", History::getSessid)
                                .addColumn("level", History::getLevel)
                                .addColumn("difficulty", History::getDifficulty)
                                .addColumn("solved", History::isSolved)
                                .addColumn("passed", History::getPassed)
                                .addColumn("passthreshold", History::getPassThreshold)
                                .addColumn("absperformance", History::getAbsperformance)
                                .addColumn("relperformance", History::getRelperformance)
                                .addColumn("ncorrect", History::getpCorrect)
                                .addColumn("nwrong", History::getpWrong)
                                .addColumn("nmissed", History::getpMissed)
                                .addColumn("ptime", History::getpTime)
                                .addColumn("maxtime", History::getMaxtime)
                                .addColumn("creationTimestamp", History::getCreationTimestamp)
                                .addColumn("finishTimestamp", History::getFinishTimestamp)
                );
        return ResponseEntity.ok(json);
    }

    @RequestMapping(value="/data/personaldata", method = RequestMethod.GET)
    public ResponseEntity<String> getPersonalDataJson()
    {
        String json = personalDataController.getAllEntities()
                .stream()
                .collect(
                        new DataJsonCollector<PersonalData>()
                                .addColumn("id", PersonalData::getId)
                                .addColumn("userid", PersonalData::getUserid)
                                .addColumn("timestamp", PersonalData::getTimestamp)
                                .addColumn("birthyear", PersonalData::getBirthyear)
                                .addColumn("gender", PersonalData::getGender)
                                .addColumn("job", PersonalData::getJob)
                                .addColumn("educationyears", PersonalData::getEducationyears)
                                .addColumn("sonnumber", PersonalData::getSonnumber)
                                .addColumn("maritalstatus", PersonalData::getMaritalstatus)
                );
        return ResponseEntity.ok(json);
    }

    @RequestMapping(value="/data/sessions", method = RequestMethod.GET)
    public ResponseEntity<String> getSessionsJson()
    {
        String json = sessionController.getAllEntities()
                .stream()
                .collect(
                        new DataJsonCollector<MSRSession>()
                                .addColumn("id", MSRSession::getId)
                                .addColumn("usergroupid", MSRSession::getUsrgrpid)
                                .addColumn("forgroup", MSRSession::getForgroup)
                                .addColumn("active", MSRSession::getActive)
                                .addColumn("difficulty", MSRSession::getDifficulty)
                                .addColumn("hospital", MSRSession::getHospital)
                                .addColumn("exercises", MSRSession::getExercises)
                );
        return ResponseEntity.ok(json);
    }

    @RequestMapping(value="/data/groups", method = RequestMethod.GET)
    public ResponseEntity<String> getGroupsJson()
    {
        String json = groupController.getAllEntities()
                .stream()
                .collect(
                        new DataJsonCollector<MSRGroup>()
                                .addColumn("id", MSRGroup::getId)
                                .addColumn("name", MSRGroup::getName)
                                .addColumn("center", MSRGroup::getCid)
                );
        return ResponseEntity.ok(json);
    }

    @RequestMapping(value="/data/centers", method = RequestMethod.GET)
    public ResponseEntity<String> getCentersJson()
    {
        String json = centerController.getAllEntities()
                .stream()
                .collect(
                        new DataJsonCollector<MSRCenter>()
                                .addColumn("id", MSRCenter::getId)
                                .addColumn("name", MSRCenter::getName)
                                .addColumn("phone", MSRCenter::getPhone)
                                .addColumn("email", MSRCenter::getEmail)
                );
        return ResponseEntity.ok(json);
    }
}

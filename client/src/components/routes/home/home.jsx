import { Fragment, useEffect, useState } from "react";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import Divider from "@mui/material/Divider";
import ListItemText from "@mui/material/ListItemText";
import ListItemAvatar from "@mui/material/ListItemAvatar";
import Avatar from "@mui/material/Avatar";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import Autocomplete from "@mui/material/Autocomplete";
import "./home.css";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { DateCalendar } from "@mui/x-date-pickers/DateCalendar";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { selectorCurrentUser } from "../../store/user/user.selector";
import Axiosapi from "../../axiosAPI/api";
const Home = () => {
  // const exampleEvents = [
  //   { label: "Brunch this weekend?", year: 1994 },
  //   { label: "Summer BBQ", year: 1972 },
  //   { label: "Oui Oui", year: 1974 },
  // ];
  const currentUser = useSelector(selectorCurrentUser);
  const [text, setText] = useState("");
  const [hostedEvent, setHostedEvent] = useState([]);
  const [allEvents, setAllEvents] = useState(null);
  const [searchEvent, setSearchEvent] = useState([]);
  const navigate = useNavigate();
  useEffect(() => {
    async function getAllHostedEvent() {
      await Axiosapi.get("/planner/events", {
        headers: {
          Authorization: `Bearer ${currentUser.token}`,
        },
      }).then((res) => {
        console.log(res);
        setHostedEvent(res.data.data);
      });
    }
    async function getAllEvents() {
      await Axiosapi.get("/public/events", {
        headers: {
          Authorization: `Bearer ${currentUser.token}`,
        },
      }).then((res) => {
        console.log(res);
        setAllEvents(res.data.data);
      });
    }
    if (currentUser.userType === "EventPlanner") {
      getAllHostedEvent();
    } else {
      getAllEvents();
    }
  }, []);
  // const inviteOtherPlanner = () => {};
  // console.log(allEvents);
  useEffect(() => {
    if (allEvents !== null) {
      const filter = allEvents.filter((event) => {
        return event.title.toLowerCase().includes(text.toLowerCase());
      });
      setSearchEvent(filter);
    }
  }, [allEvents, text]);
  console.log(searchEvent);
  return (
    <div className="homeContainer">
      <div className="eventsList">
        {currentUser.userType === "EventPlanner" ? (
          <h1>Hi EventPlanner, {currentUser.firstName}</h1>
        ) : currentUser.userType === "Customer" ? (
          <h1>Hi Customer, {currentUser.firstName}</h1>
        ) : (
          <h1>Hi Admin, {currentUser.firstName}</h1>
        )}
        <List
          sx={{ width: "100%", maxWidth: 360, bgcolor: "background.paper" }}
        >
          {currentUser.userType === "EventPlanner" &&
          hostedEvent.length === 0 ? (
            <></>
          ) : currentUser.userType === "EventPlanner" &&
            hostedEvent.length !== 0 ? (
            hostedEvent.map((hostedevent, index) => {
              const start = new Date(hostedevent.startTime);
              const end = new Date(hostedevent.endTime);
              const options = {
                year: "numeric",
                month: "short",
                day: "numeric",
                timeZone: "UTC",
                hour: "numeric",
                minute: "numeric",
              };
              const formattedstartTime = start.toLocaleString("en-AU", options);
              // console.log(formattedDate);
              const formattedendTime = end.toLocaleString("en-AU", options);
              return (
                <>
                  <ListItem
                    className="listItem"
                    alignItems="flex-start"
                    onClick={() => {
                      console.log(`hi im ${hostedevent.title}`);

                      navigate(
                        `/${hostedevent.title}/eventManage?eventId=${hostedevent.id}`
                      );
                    }}
                  >
                    <ListItemAvatar>
                      {/* <Avatar
                      alt="Remy Sharp"
                      src="/static/images/avatar/1.jpg"
                    /> */}
                    </ListItemAvatar>
                    <ListItemText
                      primary={`${hostedevent.title}`}
                      secondary={
                        <Fragment>
                          <Typography
                            sx={{ display: "inline" }}
                            component="span"
                            variant="body2"
                            color="text.primary"
                          >
                            Artist: {hostedevent.artist}
                            <br />
                          </Typography>
                          <Typography
                            sx={{ display: "inline" }}
                            component="span"
                            variant="body2"
                            color="text.primary"
                          >
                            Duration:{" "}
                            {`${formattedstartTime} - ${formattedendTime}`}
                          </Typography>
                        </Fragment>
                      }
                    />
                  </ListItem>
                  {/* <button onClick={() => inviteOtherPlanner()}>
                    invite other planner
                  </button> */}
                  <Divider variant="inset" component="li" />
                </>
              );
            })
          ) : currentUser.userType === "Customer" &&
            searchEvent.length === 0 ? (
            <></>
          ) : currentUser.userType === "Customer" &&
            searchEvent.length !== 0 ? (
            searchEvent.map((event) => {
              return (
                <>
                  <ListItem
                    className="listItem"
                    alignItems="flex-start"
                    onClick={() => {
                      console.log(`hi im ${event.title}`);

                      navigate(`/booking?eventId=${event.id}`);
                    }}
                  >
                    <ListItemAvatar>
                      {/* <Avatar
                    alt="Remy Sharp"
                    src="/static/images/avatar/1.jpg"
                  /> */}
                    </ListItemAvatar>
                    <ListItemText
                      primary={`${event.title}`}
                      secondary={
                        <Fragment>
                          <Typography
                            sx={{ display: "inline" }}
                            component="span"
                            variant="body2"
                            color="text.primary"
                          >
                            Artist: {event.artist}
                          </Typography>
                          {`${event.startTime} - ${event.endTime}`}
                        </Fragment>
                      }
                    />
                  </ListItem>
                  <Divider variant="inset" component="li" />
                </>
              );
            })
          ) : (
            searchEvent.map((event) => {
              return (
                <>
                  <ListItem
                    className="listItem"
                    alignItems="flex-start"
                    // onClick={() => {

                    //   navigate(`/booking?eventId=${event.id}`);
                    // }}
                  >
                    <ListItemAvatar>
                      {/* <Avatar
                    alt="Remy Sharp"
                    src="/static/images/avatar/1.jpg"
                  /> */}
                    </ListItemAvatar>
                    <ListItemText
                      primary={`${event.title}`}
                      secondary={
                        <Fragment>
                          <Typography
                            sx={{ display: "inline" }}
                            component="span"
                            variant="body2"
                            color="text.primary"
                          >
                            Artist: {event.artist}
                          </Typography>
                          {`${event.startTime} - ${event.endTime}`}
                        </Fragment>
                      }
                    />
                  </ListItem>
                  <Divider variant="inset" component="li" />
                </>
              );
            })
          )}
        </List>
      </div>
      {searchEvent === null ? (
        <></>
      ) : (
        <div className="searchBar-calendar">
          <Autocomplete
            inputValue={text}
            onInputChange={(e, newValue) => {
              console.log(newValue);
              setText(newValue);
            }}
            disablePortal
            id="controllable-states-demo"
            options={searchEvent.map((event) => event.title)}
            sx={{ width: 500 }}
            renderInput={(params) => (
              <TextField
                {...params}
                label="Type Music Events"
                //   onChange={(e) => {
                //     console.log(e.target.value);
                //     setText(e.target.value);
                //   }
                // }
              />
            )}
          />
          <LocalizationProvider dateAdapter={AdapterDayjs}>
            <DateCalendar sx={{ marginTop: "5rem" }} />
          </LocalizationProvider>
        </div>
      )}
    </div>
  );
};

export default Home;

import { Fragment, useEffect, useState } from "react";
import "./EventManage.css";
import Select from "@mui/material/Select";
import InputLabel from "@mui/material/InputLabel";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import Divider from "@mui/material/Divider";
import ListItemText from "@mui/material/ListItemText";
import ListItemAvatar from "@mui/material/ListItemAvatar";
import Avatar from "@mui/material/Avatar";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import Form from "../form-input/form-input";
import Button from "../button/button";
import { useLocation, useNavigate } from "react-router-dom";
import MenuItem from "@mui/material/MenuItem";
import Accordion from "@mui/material/Accordion";
import AccordionSummary from "@mui/material/AccordionSummary";
import AccordionDetails from "@mui/material/AccordionDetails";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import AxiosApi from "../axiosAPI/api";
import { useSelector } from "react-redux";
import { selectorCurrentUser } from "../store/user/user.selector";
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { MultiInputDateTimeRangeField } from "@mui/x-date-pickers-pro/MultiInputDateTimeRangeField";
const EventManage = () => {
  const [v, setV] = useState("name");
  const [hostedOrders, setHostedOrders] = useState(null);
  const [planners, setPlanners] = useState([]);
  const [planner, setPlanner] = useState("");
  const [sections, setSections] = useState(null);
  const [eventDetails, setEventDetails] = useState(null);
  const [timeRange, setTimeRange] = useState([]);
  const [venues, setVenues] = useState([{ name: "opera" }]);
  const [venue, setVenue] = useState("");
  const currentUser = useSelector(selectorCurrentUser);
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const eventId = searchParams.get("eventId");

  useEffect(() => {
    // TODO:Need to be fixed for backend
    // async function viewOrdersOfhostedEvent() {
    //   await AxiosApi.get(`planner/orders?eventId=${eventId}`, {
    //     headers: {
    //       Authorization: `Bearer ${currentUser.token}`,
    //     },
    //   }).then((res) => {
    //     console.log(res);
    //     setHostedOrders(res.data.data);
    //   });
    // }
    async function getEventDetails() {
      await AxiosApi.get(`/public/events/${eventId}`, {
        headers: {
          Authorization: `Bearer ${currentUser.token}`,
        },
      }).then((res) => {
        console.log(res);
        setEventDetails(res.data.data);
        setSections(res.data.data.sections);
        // setAllEvents(res.data.data);
      });
    }
    async function getOtherPlanner() {
      await AxiosApi.get(`/planner/invite/${eventId}`, {
        headers: {
          Authorization: `Bearer ${currentUser.token}`,
        },
      }).then((res) => {
        console.log(res);
        setPlanners(res.data.data);
        // setAllEvents(res.data.data);
      });
    }
    async function getVenues() {
      await AxiosApi.get("/public/venues", {
        headers: {
          Authorization: `Bearer ${currentUser.token}`,
        },
      }).then((res) => {
        console.log(res.data.data);
        setVenues(res.data.data);
      });
    }
    // viewOrdersOfhostedEvent();
    getEventDetails();
    getOtherPlanner();
    getVenues();
  }, []);
  const handleDateTimeRangeChange = (value) => {
    // console.log("Received value:", context);
    setTimeRange({
      value,
    });
  };
  // const handleDateTimeRangeChange = (value) => {
  //   // console.log("Received value:", context);
  //   setTimeRange({
  //     value,
  //   });
  // };
  const navigate = useNavigate();

  const modifyEvent = async (e) => {
    e.preventDefault();
    console.log(timeRange);
    const selectedVenue = venues.filter((v) => v.name === venue);
    if (timeRange.value !== undefined) {
      if (
        timeRange.value[0].$d == null ||
        timeRange.value[1].$d == null ||
        timeRange.value[0].$d === "Invalid Date" ||
        timeRange.value[1].$d === "Invalid Date"
      ) {
        await AxiosApi.post(
          "/planner/events",
          {
            id: eventId,
            title: eventDetails.title,
            artist: eventDetails.artist,
            startTime: eventDetails.startTime,
            endTime: eventDetails.endTime,
            venueId: selectedVenue[0].id,
            status: 1,
            sections,
          },
          {
            headers: {
              Authorization: `Bearer ${currentUser.token}`,
            },
          }
        ).then((res) => console.log(res));
      } else {
        await AxiosApi.post(
          "/planner/events",
          {
            id: eventId,
            title: eventDetails.title,
            artist: eventDetails.artist,
            startTime: timeRange.value[0].$d,
            endTime: timeRange.value[1].$d,
            venueId: selectedVenue[0].id,
            status: 1,
            sections,
          },
          {
            headers: {
              Authorization: `Bearer ${currentUser.token}`,
            },
          }
        ).then((res) => console.log(res));
      }
    } else {
      await AxiosApi.post(
        "/planner/events",
        {
          id: eventId,
          title: eventDetails.title,
          artist: eventDetails.artist,
          startTime: eventDetails.startTime,
          endTime: eventDetails.endTime,
          venueId: selectedVenue[0].id,
          status: 1,
          sections,
        },
        {
          headers: {
            Authorization: `Bearer ${currentUser.token}`,
          },
        }
      ).then((res) => console.log(res));
    }
  };
  const cancelEvent = () => {};
  const cancelOrder = async (orderId) => {
    await AxiosApi.post(
      `/planner/orders/cancel/${orderId}`,
      {},
      {
        headers: {
          Authorization: `Bearer ${currentUser.token}`,
        },
      }
    ).then((res) => {
      console.log(res);
    });
  };
  // console.log(eventDetails);
  // console.log(sections);
  // console.log(venue);
  return (
    <div className="homeContainer">
      <div className="eventsList">
        <h1>customer booking list for this event</h1>
        <List
          sx={{ width: "100%", maxWidth: 360, bgcolor: "background.paper" }}
        >
          {hostedOrders == null ? (
            <></>
          ) : (
            hostedOrders.map((order) => {
              const start = new Date(order.event.startTime);
              const end = new Date(order.event.endTime);
              const createdAt = new Date(order.createdAt);
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
              const formattedcreateAt = createdAt.toLocaleString(
                "en-AU",
                options
              );
              return (
                <>
                  <ListItem
                    className="listItem"
                    alignItems="flex-start"
                    onClick={() => {
                      // TODO: Soft coded event name
                      navigate("/Summer BBQ/booking");
                    }}
                  >
                    <ListItemAvatar></ListItemAvatar>
                    <ListItemText
                      primary={`${order.event.title}`}
                      secondary={
                        <Fragment>
                          <Typography
                            sx={{ display: "inline" }}
                            component="span"
                            variant="body2"
                            color="text.primary"
                          >
                            CreateAt: {formattedcreateAt}
                            <br />
                          </Typography>
                          <Typography
                            sx={{ display: "inline" }}
                            component="span"
                            variant="body2"
                            color="text.primary"
                          >
                            Artist: {order.event.artist}
                            <br />
                          </Typography>
                          <Typography
                            sx={{ display: "inline" }}
                            component="span"
                            variant="body2"
                            color="text.primary"
                          >
                            Duration: {formattedstartTime} - {formattedendTime}
                            <br />
                          </Typography>
                          <Typography
                            sx={{ display: "inline" }}
                            component="span"
                            variant="body2"
                            color="text.primary"
                          >
                            Status: {order.status}
                          </Typography>
                        </Fragment>
                      }
                    />
                  </ListItem>
                  <button onClick={() => cancelOrder(order.id)}>
                    cancel booking
                  </button>
                  <Divider variant="inset" component="li" />
                </>
              );
            })
          )}
        </List>
      </div>
      <div className="searchBar-calendar">
        <div className="event-creation-container">
          <div className="creation-container">
            <form onSubmit={modifyEvent}>
              {eventDetails == null ? (
                <></>
              ) : (
                <>
                  <Form
                    label="Event Title"
                    type="text"
                    required
                    name="Title"
                    value={eventDetails.title}
                    onChange={(e) =>
                      setEventDetails((prev) => ({
                        ...prev,
                        title: e.target.value,
                      }))
                    }
                  ></Form>
                  <Form
                    label="Artist"
                    type="text"
                    required
                    //   onChange={(e) => setArtist(e.target.value)}
                    name="Artist"
                    value={eventDetails.artist}
                    onChange={(e) =>
                      setEventDetails((prev) => ({
                        ...prev,
                        artist: e.target.value,
                      }))
                    }
                  ></Form>
                  <LocalizationProvider dateAdapter={AdapterDayjs}>
                    <DemoContainer
                      components={[
                        "MultiInputDateTimeRangeField",
                        "SingleInputDateTimeRangeField",
                      ]}
                    >
                      <MultiInputDateTimeRangeField
                        slotProps={{
                          textField: ({ position }) => ({
                            label:
                              position === "start" ? "StartTime" : "endTime",
                          }),
                        }}
                        onChange={handleDateTimeRangeChange}
                        // value={[eventDetails.startTime, eventDetails.endTime]}
                        // onChange={handleDateTimeRangeChange}
                      />
                    </DemoContainer>
                  </LocalizationProvider>
                </>
              )}
            </form>
          </div>
          <div className="section-creation-container">
            <div className="section-setting">
              <p style={{ marginTop: "3.5rem" }}>Section Settings</p>
              {/* <button
                id="section-setting-button"
                onClick={() => {
                  console.log("hit me");
                }}
              >
                <AddIcon />
              </button> */}
            </div>
            {sections == null ? (
              <></>
            ) : (
              sections.map((section, index) => {
                return (
                  <Accordion sx={{ marginTop: "1rem", width: "100%" }}>
                    <AccordionSummary
                      expandIcon={<ExpandMoreIcon />}
                      aria-controls="panel1a-content"
                      id="panel1a-header"
                    >
                      <Typography>Section {index + 1}</Typography>
                    </AccordionSummary>
                    <AccordionDetails>
                      <Form
                        label="Section Name"
                        type="text"
                        required
                        //   onChange={(e) => setSectionName(e.target.value)}
                        name="Section Name"
                        value={section.name}
                        onChange={(e) => {
                          const updatedSection = sections.map((sec) => {
                            if (section.id === sec.id) {
                              return {
                                ...section,
                                name: e.target.value,
                              };
                            }
                            return sec;
                          });
                          setSections(updatedSection);
                        }}
                      ></Form>
                      <Form
                        label="Price"
                        type="number"
                        required
                        //   onChange={(e) => setPrice(e.target.value)}
                        name="Unit price"
                        value={section.unitPrice}
                        onChange={(e) => {
                          const updatedSection = sections.map((sec) => {
                            if (section.id === sec.id) {
                              return {
                                ...section,
                                unitPrice: Number(e.target.value),
                              };
                            }
                            return sec;
                          });
                          setSections(updatedSection);
                        }}
                      ></Form>
                      <Form
                        label="Capacity"
                        type="number"
                        required
                        //   onChange={(e) => setCapacity(e.target.value)}
                        name="Capacity"
                        value={section.capacity}
                        onChange={(e) => {
                          const updatedSection = sections.map((sec) => {
                            if (section.id === sec.id) {
                              return {
                                ...section,
                                capacity: Number(e.target.value),
                              };
                            }
                            return sec;
                          });
                          setSections(updatedSection);
                        }}
                      ></Form>
                    </AccordionDetails>
                  </Accordion>
                );
              })
            )}
            <InputLabel id="demo-simple-select-filled-label">
              Venue(Reuqired)
            </InputLabel>
            <Select
              sx={{ width: "200px" }}
              labelId="demo-simple-select-filled-label"
              id="demo-simple-select-filled"
              value={venue}
              onChange={(e) => setVenue(e.target.value)}
            >
              {venues.length === 0 ? (
                <></>
              ) : (
                venues.map((venue) => (
                  <MenuItem
                    key={venue.id}
                    value={venue.name}
                    onInput={(e) => console.log(e)}
                  >
                    {venue.name}
                  </MenuItem>
                ))
              )}
            </Select>
            <InputLabel
              id="demo-simple-select-filled-label"
              sx={{ marginTop: "1rem" }}
            >
              Invite other planners(option)
            </InputLabel>
            <Select
              sx={{ width: "200px" }}
              labelId="demo-simple-select-filled-label"
              id="demo-simple-select-filled"
              value={planner}
              onChange={(e) => setPlanner(e.target.value)}
            >
              {planners.length === 0 ? (
                <></>
              ) : (
                planners.map((planner) => (
                  <MenuItem
                    key={planner.id}
                    value={planner.id}
                    // onInput={(e) => console.log(e)}
                  >
                    {planner.firstName} {planner.lastName}
                  </MenuItem>
                ))
              )}
            </Select>
          </div>
        </div>
        <div className="button-group">
          <button id="modify-event-button" onClick={(e) => modifyEvent(e)}>
            Modify event
          </button>
          <button id="cancel-event-button" onClick={() => cancelEvent()}>
            cancel event
          </button>
        </div>
      </div>
    </div>
  );
};

export default EventManage;

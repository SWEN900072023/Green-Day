import "./eventCreation.scss";
import Form from "../../form-input/form-input";
import Button from "../../button/button";
import Select from "@mui/material/Select";
import InputLabel from "@mui/material/InputLabel";
import { useEffect, useState } from "react";
import Axiosapi from "../../axiosAPI/api";
import Accordion from "@mui/material/Accordion";
import AccordionSummary from "@mui/material/AccordionSummary";
import AccordionDetails from "@mui/material/AccordionDetails";
import Typography from "@mui/material/Typography";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import AddIcon from "@mui/icons-material/Add";
import TextField from "@mui/material/TextField";
import MenuItem from "@mui/material/MenuItem";
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { MultiInputDateTimeRangeField } from "@mui/x-date-pickers-pro/MultiInputDateTimeRangeField";

import { useSelector } from "react-redux";
import { selectorCurrentUser } from "../../store/user/user.selector";

const EventCreation = () => {
  const currentUser = useSelector(selectorCurrentUser);
  const currencies = [
    {
      value: "USD",
      label: "$",
    },
    {
      value: "EUR",
      label: "€",
    },
    {
      value: "BTC",
      label: "฿",
    },
    {
      value: "JPY",
      label: "¥",
    },
  ];
  const [accordions, setAccordions] = useState([]);
  const [title, setTitle] = useState("");
  const [artist, setArtist] = useState("");
  const [timeRange, setTimeRange] = useState([]);
  // const [startTime, setStartTime] = useState("");
  // const [endTime, setEndTime] = useState("");
  const [sections, setSections] = useState("");
  const [venues, setVenues] = useState([{ name: "opera" }]);
  const [venue, setVenue] = useState("");
  // console.log(sections);
  const createNewElement = () => {
    setAccordions((prevAccordions) => [
      ...prevAccordions,
      { name: "", currency: "AUD", unitPrice: "", capacity: "" },
    ]);
  };
  useEffect(() => {
    async function getVenues() {
      await Axiosapi.get("/public/venues", {
        headers: {
          Authorization: `Bearer ${currentUser.token}`,
        },
      }).then((res) => {
        console.log(res.data.data);
        setVenues(res.data.data);
      });
    }

    getVenues();
  }, []);
  useEffect(() => {
    setSections(accordions);
  }, [accordions]);
  const handleDateTimeRangeChange = (value) => {
    // console.log("Received value:", context);
    setTimeRange({
      value,
    });
  };
  // console.log(timeRange);
  const createEvent = async (e) => {
    e.preventDefault();
    // console.log(sections);
    console.log(timeRange);
    // console.log(Date(timeRange.value[0].$d));
    const selectedVenue = venues.filter((v) => v.name === venue);
    await Axiosapi.post(
      "/planner/events",
      {
        title,
        artist,
        startTime: timeRange.value[0].$d,
        endTime: timeRange.value[1].$d,
        sections,
        venueId: selectedVenue[0].id,
      },
      {
        headers: {
          Authorization: `Bearer ${currentUser.token}`,
        },
      }
    ).then((res) => {
      console.log(res);
      alert("event create successfully");
    });
  };
  // console.log(venue);
  return (
    // <div className="container">
    <div className="Contain">
      <h1>im event creation</h1>
      <div className="event-creation-container">
        <div className="creation-container">
          <Form
            style={{ marginBottom: "2px" }}
            label="Event Title"
            type="text"
            required
            name="Title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          ></Form>
          <Form
            // style={{ marginTop: "0.5rem" }}
            label="Artist"
            type="text"
            required
            onChange={(e) => setArtist(e.target.value)}
            name="Artist"
            value={artist}
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
                    label: position === "start" ? "StartTime" : "endTime",
                  }),
                }}
                onChange={handleDateTimeRangeChange}
              />
            </DemoContainer>
          </LocalizationProvider>
        </div>
        <div className="section-creation-container">
          <div className="section-setting">
            <p style={{ marginTop: "3.5rem" }}>Section Settings</p>
            <button
              id="section-setting-button"
              onClick={() => {
                console.log("hit me");
                createNewElement();
              }}
            >
              <AddIcon />
            </button>
          </div>
          {accordions.length === 0 ? (
            <></>
          ) : (
            accordions.map((accordion, index) => {
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
                      onChange={(e) => {
                        const updatedAccordions = accordions.map(
                          (accordion, accIndex) => {
                            if (index === accIndex) {
                              return { ...accordion, name: e.target.value };
                            }
                            return accordion;
                          }
                        );
                        setAccordions(updatedAccordions);
                      }}
                      name="Section Name"
                      value={accordions[index].name}
                    ></Form>
                    <Form
                      label="Price"
                      type="number"
                      required
                      onChange={(e) => {
                        const updatedAccordions = accordions.map(
                          (accordion, accIndex) => {
                            if (index === accIndex) {
                              return {
                                ...accordion,
                                unitPrice: e.target.value,
                              };
                            }
                            return accordion;
                          }
                        );
                        setAccordions(updatedAccordions);
                      }}
                      name="Price"
                      value={accordions[index].unitPrice}
                    ></Form>
                    <Form
                      label="Capacity"
                      type="number"
                      required
                      onChange={(e) => {
                        const updatedAccordions = accordions.map(
                          (accordion, accIndex) => {
                            if (index === accIndex) {
                              return { ...accordion, capacity: e.target.value };
                            }
                            return accordion;
                          }
                        );
                        setAccordions(updatedAccordions);
                      }}
                      name="Capacity"
                      value={accordions[index].capacity}
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
        </div>
      </div>
      {/* <Button type="click" buttonType="google" onClick={() => createEvent()}>
        Create event
      </Button> */}
      <button id="create-event-button" onClick={(e) => createEvent(e)}>
        Create event
      </button>
    </div>
    // </div>
  );
};

export default EventCreation;

import "./eventCreation.scss";
import Form from "../../form-input/form-input";
import Button from "../../button/button";
import { useState } from "react";
import { createRoot } from "react-dom/client";
import Axiosapi from "../../axiosAPI/api";
import Accordion from "@mui/material/Accordion";
import AccordionSummary from "@mui/material/AccordionSummary";
import AccordionDetails from "@mui/material/AccordionDetails";
import Typography from "@mui/material/Typography";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import AddIcon from "@mui/icons-material/Add";
import TextField from "@mui/material/TextField";
import MenuItem from "@mui/material/MenuItem";

const EventCreation = () => {
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
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");
  const [sectionName, setSectionName] = useState("");
  const [price, setPrice] = useState("");
  const [capacity, setCapacity] = useState("");
  //   const signUp = async (e) => {
  //     e.preventDefault();
  //     await Axiosapi.post("/register", {
  //       email,
  //       lastName,
  //       firstName,
  //       password,
  //     }).then((res) => console.log(res));
  //   };
  const createNewElement = () => {
    // const container = document.getElementById("accordion");
    const accordionElement = (
      <Accordion sx={{ marginTop: "1rem" }}>
        <AccordionSummary
          expandIcon={<ExpandMoreIcon />}
          aria-controls="panel1a-content"
          id="panel1a-header"
        >
          <Typography>Accordion 1</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <Typography>
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse
            malesuada lacus ex, sit amet blandit leo lobortis eget.
          </Typography>
        </AccordionDetails>
      </Accordion>
    );
    // (accordionElement, container);
    setAccordions((prevAccordions) => [...prevAccordions, accordionElement]);
  };
  const createEvent = () => {};
  return (
    // <div className="container">
    <div className="Contain">
      <h1>im event creation</h1>
      <div className="event-creation-container">
        <div className="creation-container">
          <form onSubmit={createEvent}>
            <Form
              label="Event Title"
              type="text"
              required
              name="Title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
            ></Form>
            <Form
              label="Artist"
              type="text"
              required
              onChange={(e) => setArtist(e.target.value)}
              name="Artist"
              value={artist}
            ></Form>
            <Form
              label="Start Time"
              type="date"
              required
              onChange={(e) => setStartTime(e.target.value)}
              name="Start Time"
              value={startTime}
            ></Form>
            <Form
              label="End Time"
              type="date"
              required
              onChange={(e) => setEndTime(e.target.value)}
              name="End Time"
              value={endTime}
            ></Form>
          </form>
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
          {accordions == [] ? (
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
                      onChange={(e) => setSectionName(e.target.value)}
                      name="Section Name"
                      value={sectionName}
                    ></Form>
                    <Form
                      label="Price"
                      type="number"
                      required
                      onChange={(e) => setPrice(e.target.value)}
                      name="Section Name"
                      value={price}
                    ></Form>
                    <Form
                      label="Capacity"
                      type="number"
                      required
                      onChange={(e) => setCapacity(e.target.value)}
                      name="Capacity"
                      value={capacity}
                    ></Form>
                  </AccordionDetails>
                </Accordion>
              );
            })
          )}
          <TextField
            sx={{ marginTop: "2rem" }}
            id="outlined-select-currency"
            select
            label="Invite Planner(option)"
            defaultValue="EUR"
            helperText="Please select other palnner"
          >
            {currencies.map((option) => (
              <MenuItem key={option.value} value={option.value}>
                {option.label}
              </MenuItem>
            ))}
          </TextField>
        </div>
      </div>
      <Button type="submit" buttonType="google">
        Create event
      </Button>
    </div>
    // </div>
  );
};

export default EventCreation;

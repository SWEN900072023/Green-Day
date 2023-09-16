import { Fragment, useEffect, useState } from "react";
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
import { useNavigate } from "react-router-dom";
import MenuItem from "@mui/material/MenuItem";
import Accordion from "@mui/material/Accordion";
import AccordionSummary from "@mui/material/AccordionSummary";
import AccordionDetails from "@mui/material/AccordionDetails";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import AddIcon from "@mui/icons-material/Add";

const EventManage = () => {
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
  const accordions = [
    {
      name: "VIP",
      price: "40",
      currency: "AUD",
      capacity: "30",
      remaining: "15",
    },
    {
      name: "Ordinary",
      price: "25",
      currency: "AUD",
      capacity: "30",
      remaining: "5",
    },
  ];
  const exampleEvents = [
    { label: "Brunch this weekend?", year: 1994 },
    { label: "Summer BBQ", year: 1972 },
    { label: "Oui Oui", year: 1974 },
  ];
  const [text, setText] = useState("");
  const navigate = useNavigate();

  const searchEvent = exampleEvents.filter((event) => {
    return event.label.toLowerCase().includes(text.toLowerCase());
  });
  const modifyEvent = () => {};
  return (
    <div className="homeContainer">
      <div className="eventsList">
        {/* TODO: For event planner, it should display the event that they hosted */}
        <h1>customer booking list for this event</h1>
        <List
          sx={{ width: "100%", maxWidth: 360, bgcolor: "background.paper" }}
        >
          {searchEvent.map((event) => {
            return (
              <>
                <ListItem
                  className="listItem"
                  alignItems="flex-start"
                  onClick={() => {
                    console.log(`hi im ${event.label}`);
                    // TODO: Soft coded event name
                    navigate("/Summer BBQ/booking");
                  }}
                >
                  <ListItemAvatar>
                    <Avatar
                      alt="Remy Sharp"
                      src="/static/images/avatar/1.jpg"
                    />
                  </ListItemAvatar>
                  <ListItemText
                    primary={`${event.label}`}
                    secondary={
                      <Fragment>
                        <Typography
                          sx={{ display: "inline" }}
                          component="span"
                          variant="body2"
                          color="text.primary"
                        >
                          Ali Connors
                        </Typography>
                        {" — I'll be in your neighborhood doing errands this…"}
                      </Fragment>
                    }
                  />
                </ListItem>
                <Divider variant="inset" component="li" />
              </>
            );
          })}
        </List>
      </div>
      <div className="searchBar-calendar">
        <div className="event-creation-container">
          <div className="creation-container">
            <form onSubmit={modifyEvent}>
              <Form
                label="Event Title"
                type="text"
                required
                name="Title"
                value=""
                //   onChange={(e) => setTitle(e.target.value)}
              ></Form>
              <Form
                label="Artist"
                type="text"
                required
                //   onChange={(e) => setArtist(e.target.value)}
                name="Artist"
                value=""
              ></Form>
              <Form
                label="Start Time"
                type="date"
                required
                //   onChange={(e) => setStartTime(e.target.value)}
                name="Start Time"
                value=""
              ></Form>
              <Form
                label="End Time"
                type="date"
                required
                //   onChange={(e) => setEndTime(e.target.value)}
                name="End Time"
                value=""
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
                        //   onChange={(e) => setSectionName(e.target.value)}
                        name="Section Name"
                        value={accordion.name}
                      ></Form>
                      <Form
                        label="Price"
                        type="number"
                        required
                        //   onChange={(e) => setPrice(e.target.value)}
                        name="Section Name"
                        value={accordion.price}
                      ></Form>
                      <Form
                        label="Capacity"
                        type="number"
                        required
                        //   onChange={(e) => setCapacity(e.target.value)}
                        name="Capacity"
                        value={accordion.capacity}
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
    </div>
  );
};

export default EventManage;

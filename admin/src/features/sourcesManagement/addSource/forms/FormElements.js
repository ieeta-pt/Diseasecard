import TextField from "@material-ui/core/TextField";
import React from "react";
import FormHelperText from "@material-ui/core/FormHelperText";
import {makeStyles} from "@material-ui/core/styles";
import DropZone from "react-dropzone";
import {Col, Row} from "react-bootstrap";
import {
    Button, Chip,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    FormLabel, Input, Select
} from "@material-ui/core";
import Switch from "@material-ui/core/Switch";
import FormControlLabel from "@material-ui/core/FormControlLabel";

const renderFromHelper = ({ touched, error, labelText }) => {
    if (!(touched && error)) {
        return <FormLabel  style={{marginLeft: "2px", fontSize: "0.75rem"}} >{labelText}</FormLabel>
    } else {
        return <FormHelperText>{touched && error}</FormHelperText>
    }
}


export const useStyles = makeStyles((theme) => ({
    button: {
        borderRadius: "15px",
        boxShadow: "0 5px 10px 0 rgba(0,0,0,0.2)",
        padding: "0.5px 10px 1px 10px",
        fontSize: "14px",
        textTransform: "none",
        borderColor: "#E94F3F",
        color: "#E94F3F",
        '&:hover':{
            borderColor: "#f3ab9b",
            color: "#f3ab9b"
        },
    },
    buttonG: {
        borderRadius: "15px",
        boxShadow: "0 5px 10px 0 rgba(0,0,0,0.2)",
        padding: "0.5px 10px 1px 10px",
        fontSize: "14px",
        textTransform: "none",
        borderColor: "#f3ab9b",
        color: "#fff",
        '&:hover':{
            borderColor: "#E94F3F",
        },
        background: "linear-gradient(-135deg, #E94F3F 0%, #f3ab9b 100%)"
    },
    buttonP: {
        borderRadius: "15px",
        boxShadow: "0 5px 10px 0 rgba(0,0,0,0.2)",
        padding: "0.5px 10px 1px 10px",
        marginRight: "5%",
        fontSize: "14px",
        textTransform: "none",
        borderColor: "#ffccbc",
        color: "#fff",
        '&:hover':{
            borderColor: "#fbe9e7",
        },
        background: "linear-gradient(-135deg, #f3ab9b 0%, #fbe9e7 100%)"
    },
    field: {
        borderColor: "#f3ab9b",
        '& label.Mui-focused': {
            color: "#f3ab9b",
        },
        '& .MuiInput-underline:after': {
            borderBottomColor: "#f3ab9b",
        },
        '& .MuiOutlinedInput-root': {
            '&:hover fieldset': {
                borderColor: "#f3ab9b",
            },
            '&.Mui-focused fieldset': {
                borderColor: "#f3ab9b",
            },
        }
    },
    switch: {
        borderColor: "#E94F3F",
        '& .MuiSwitch-colorPrimary.Mui-checked': {
            color: "#E94F3F",
        },
        '& .MuiSwitch-colorPrimary.Mui-checked + .MuiSwitch-track': {
            backgroundColor: "#E94F3F",
        },
        width: "100%"
    },
    root: {
        '& .MuiTextField-root': {
            margin: theme.spacing(2),
            width: '100%',
        },
        '& .MuiButton-outlined.Mui-disabled': {
            background: "#748892"
        },
        '& .MuiButton-root.Mui-disabled': {
            color: "#fff"
        },
    },
}));


export const renderTextField = ({ label, input, value, disabled=false, labelText, meta: { touched, invalid, error },  ...custom }) => (
    <TextField
        label={label}
        placeholder={label}
        error={touched && invalid}
        value={value}
        helperText={renderFromHelper({ touched, error, labelText })}
        {...input}
        {...custom}
        style={{width: "100%"}}
        disabled={disabled}
    />
)


export const renderSwitchField = ({ label, size, checked, input, labelText, labelPlacement, meta: { touched, invalid, error },  ...custom }) => (
    <FormControlLabel
        control={<Switch color="primary" size={size}/>}
        labelPlacement={labelPlacement}
        label={label}
        placeholder={label}
        checked={checked}
        {...input}
        {...custom}
    />
)


export const renderSelectField = ({ input ,label, labelText, meta: { touched, invalid, error }, children, ...custom}) => (
    <TextField
        id="outlined-select-currency"
        select
        variant="outlined"
        label={label}
        placeholder={label}
        error={touched && invalid}
        style={{width: "100%"}}
        helperText={renderFromHelper({ touched, error, labelText })}
        {...input}
        {...custom}
    >
        {children}
    </TextField>
)


export const renderMultipleSelectField = ({ value, onChange, classes, MenuProps, children }) => (
    <Select
        labelId="demo-mutiple-chip-label"
        id="demo-mutiple-chip"
        multiple
        value={value}
        onChange={onChange}
        input={<Input id="select-multiple-chip" />}
        MenuProps={MenuProps}
        rendervalue={(selected) => {
            return(
                <div className={classes.chips}>
                    {selected.map((value) => (
                        <Chip key={value} label={value} className={classes.chip}/>
                    ))}
                </div>
            )
        }}
    >
        {children}
    </Select>
)


export const renderDropzoneInput = (field) => {
    let f = field.input.value || null;
    const style = field.style

    return (
        <div>
            <DropZone
                name={field.name}
                onDrop={( filesToUpload ) => { field.input.onChange(filesToUpload[0])}}
                maxFiles={1}>
                {({getRootProps, getInputProps}) => (
                    <section>
                        <div {...getRootProps({style})}>
                            <input {...getInputProps()} />
                            {f &&  <p style={{marginBottom: "0", color: "#888"}}><b>Selected File:</b>&nbsp;{f.name}</p>}
                            {f==null && <p style={{marginBottom: "0", color: "#888"}}>Drag 'n' drop a file here, or click to select one</p> }
                        </div>
                    </section>
                )}
            </DropZone>
        </div>
    );
}



export const FootForm = (props, c, type) => {
    const [open, setOpen] = React.useState(false);

    const handleClickOpen = () => {
        if (props.valid) setOpen(true);
    };
    const goBack = () => {
        if ( type=== 'Parser' ) props.formDetails.goToStep(8);
        else                    props.formDetails.goToStep(5);
    };
    const handleClose = () => {
        setOpen(false);
        props.formDetails.goToStep(5);
    };
    const handleFinish = () => {
        setOpen(false);
        props.formDetails.goToStep(1);
    };

    return (
        <div style={{ marginTop: '20px', marginBottom: "20px", width: "100%"}}>
            <Row className="justify-content-md-center">
                <Col sm="6" className="centerStuff">
                    <Button variant="outlined" color="primary" className={ c.buttonP } type="button" onClick={goBack}>
                        Go Back
                    </Button>
                </Col>
                <Col sm="6" className="centerStuff">
                    <Button variant="outlined" className={ c.buttonG } type="submit" disabled={ props.pristine || props.submitting || props.invalid } onClick={handleClickOpen}>
                        Submit
                    </Button>
                    <Dialog open={open} onClose={handleClose} aria-labelledby="alert-dialog-title" aria-describedby="alert-dialog-description">
                        <DialogTitle id="alert-dialog-title">{"Use Google's location service?"}</DialogTitle>
                        <DialogContent>
                            <DialogContentText id="alert-dialog-description">
                                //TODO add text
                            </DialogContentText>
                        </DialogContent>
                        <DialogActions style={{width: "100%", display: "block"}}>
                            <div style={{ marginTop: '20px', marginBottom: "20px"}}>
                                <Row className="justify-content-md-center">
                                    <Col sm="6" className="centerStuff">
                                        <a href="#" className='label theme-bg text-white f-14' style={{ borderRadius: "15px", boxShadow: "0 5px 10px 0 rgba(0,0,0,0.2)" }} onClick={ handleClose }>Add More</a>
                                    </Col>
                                    <Col sm="6" className="centerStuff">
                                        <a href="#" className={'label theme-bg text-white f-14'} style={{ borderRadius: "15px", boxShadow: "0 5px 10px 0 rgba(0,0,0,0.2)" }}  onClick={handleFinish}> Finish </a>
                                    </Col>
                                </Row>
                            </div>
                        </DialogActions>
                    </Dialog>
                </Col>
            </Row>
        </div>
    )
}
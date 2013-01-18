android-quickform
=================

Simple form generator for Android. Inspired by ios QuickDialog form library.

To create a form, check the demo project for example. 
A form is made up of different sections which consists of different form elements.

## Note 
- HRootElement: This element represents a form
- HSection: Represent a section in the form
- HElement: an element in the form

## Form Elements
- HDatePickerElement: allow user to select date
- HNumericElement: a textfield which only allow digits entered. The EditText has numeric keyboard
- HPickerElement: allow user to select an item from single choice dialog
- HTextAreaEntryElement: Mutiple-lines EditText
- HTextEntryElement: Single line EditText
- HTextElement: text (not tested)

## Form Data Store

- HPrefDataStore: allow auto-saving of form data to SharedPreferences

## Form Base Activity

- All screens to extend from HBaseFormActivity
- Just need to override createRootElement() to create a root form element

package fr.heavencraft.async.actions;

public interface Action
{
	void executeAction() throws Exception;

	void onSuccess();

	void onFailure();
}
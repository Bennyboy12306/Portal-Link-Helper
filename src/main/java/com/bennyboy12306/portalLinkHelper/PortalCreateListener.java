package com.bennyboy12306.portalLinkHelper;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This class handles the portal create event and sends a message with link coordinates when a portal is made.
 * @author Bennyboy12306
 */

public class PortalCreateListener implements Listener
{
    /**
     * This method checks if the portal was created rather than generated if so it returns the link coordinates based on the dimension the portal is in to the player that created it.
     * @param event the portal created event that triggered this method to be called.
     */
    @EventHandler
    public void onPortalCreated(PortalCreateEvent event)
    {
        //Do nothing if the portal was generated rather than created.
        if (event.getReason() != PortalCreateEvent.CreateReason.FIRE)
        {
            return;
        }

        if (event.getEntity() instanceof Player player)
        {
            Block middle = findMiddle(event);
            String creationCoords = "X: " + middle.getX() + " Z: " + middle.getZ();
            String linkCoords;

            // Perform the portal link coordinate calculation and output the message based on the dimension the portal was created in
            if (event.getWorld().getEnvironment() == World.Environment.NORMAL)
            {
                linkCoords = "X: " + Math.round(middle.getX()/8.0) + " Z: " + Math.round(middle.getZ()/8.0);
                player.sendMessage("§2You created a portal at " + creationCoords + " in the Overworld. §5You should link this portal at " + linkCoords + " in the Nether.");
            }
            else
            {
                linkCoords = "X: " + Math.round(middle.getX()*8.0) + " Z: " + Math.round(middle.getZ()*8.0);
                player.sendMessage("§5You created a portal at " + creationCoords + " in the Nether. §2You should link this portal at " + linkCoords + " in the Overworld.");
            }
        }
    }

    /**
     * This method calculates the middle block of the portal frame.
     * @param event the portal created event.
     * @return the middle portal frame block.
     */
    private static Block findMiddle(PortalCreateEvent event)
    {
        // Find a block on the lowest layer of the portal.
        Block lowestBlock = getBlock(event);

        // Find portal orientation.
        boolean isFacingX = isFacingX(event, lowestBlock);

        // Find the middle block or left of the middle when the middle is 2 wide.
        Block middleBlock;
        List<Block> obsidianBlocks = new ArrayList<>();
        int portalWidth;
        int middleIndex;

        if (isFacingX)
        {
            for (BlockState state: event.getBlocks())
            {
                Block block = state.getBlock();
                if (block.getType() == Material.OBSIDIAN && block.getZ() == lowestBlock.getZ())
                {
                    obsidianBlocks.add(block);
                }
            }

            // Sort the blocks by x coordinate ascending.
            obsidianBlocks.sort(Comparator.comparingInt(Block::getX));
        }
        else
        {
            for (BlockState state: event.getBlocks())
            {
                Block block = state.getBlock();
                if (block.getType() == Material.OBSIDIAN && block.getX() == lowestBlock.getX())
                {
                    obsidianBlocks.add(block);
                }
            }

            // Sort the blocks by z coordinate ascending.
            obsidianBlocks.sort(Comparator.comparingInt(Block::getZ));

        }
        portalWidth = obsidianBlocks.size();
        middleIndex = portalWidth / 2;
        if (portalWidth % 2 == 0)
        {
            middleBlock = obsidianBlocks.get(middleIndex -1);
        }
        else
        {
            middleBlock = obsidianBlocks.get(middleIndex);
        }

        return middleBlock;
    }

    /**
     * This method gets a block from the lowest layer of the portal.
     * @param event the portal created event.
     * @return a block from the lowest layer of the portal.
     */
    private static @NotNull Block getBlock(PortalCreateEvent event) {
        Block lowestBlock = null;
        for (BlockState state: event.getBlocks())
        {
            Block block = state.getBlock();
            if (block.getType() == Material.OBSIDIAN)
            {
                if (lowestBlock == null || block.getY() < lowestBlock.getY())
                {
                    lowestBlock = block;
                }
            }
        }

        assert lowestBlock != null; //At this stage lowestBlock should never be null, this line simply suppresses warnings.
        return lowestBlock;
    }

    /**
     * This method calculates which way the portal is facing.
     * @param event the portal created event.
     * @param lowestBlock a block from the bottom layer of the portal.
     * @return if the portal is facing the x-axis.
     */

    private static boolean isFacingX(PortalCreateEvent event, Block lowestBlock) {
        boolean isFacingX;
        int obsidianCountX = 0;
        int obsidianCountZ = 0;

        for (BlockState state: event.getBlocks())
        {
            Block block = state.getBlock();
            if (block.getType() == Material.OBSIDIAN)
            {
                if (block.getX() == lowestBlock.getX())
                {
                    obsidianCountX++;
                }
                if (block.getZ() == lowestBlock.getZ())
                {
                    obsidianCountZ++;
                }
            }
        }

        // If obsidianCountX < obsidianCountZ it is facing the x-axis, otherwise it is facing the z-axis.
        isFacingX = obsidianCountX < obsidianCountZ;
        return isFacingX;
    }
}
